package se.ramn.migrations

import java.util.UUID
import org.mapdb.DB
import collection.JavaConverters._

import se.ramn.models.Db
import se.ramn.models.DbTable


object Migrations {
  val migrations: Map[Int, DB => Unit] = Map.empty +
    (1 -> migrateBotV1toV2 _) +
    (2 -> swapBotIdToBotVersionIdInBattlesTable _) +
    (3 -> renameJarsOnDisk _)

  def migrateBotV1toV2(db: DB) = {
    import se.ramn.models.{v1, v2}
    import se.ramn.models.Bot

    db.rename("bots", "bots_v1")
    val oldBotsTable = db.getTreeMap[UUID, Bot]("bots_v1").asScala
    val newBotsTable = db.getTreeMap[UUID, v2.Bot]("bots_v2").asScala
    val botVersionsTable =
      db.getTreeMap[UUID, v2.BotVersion](DbTable.botVersions.toString).asScala
    val usersTable =
      db.getTreeMap[UUID, v1.User](DbTable.users.toString).asScala

    oldBotsTable foreach { case (oldBotId, oldBot) =>
      val user = v1.User(name=oldBot.originalFilename)
      val newBot = v2.Bot(
        id=oldBot.id,
        name=oldBot.originalFilename,
        ownerId=user.id,
        createdAt=oldBot.createdAt)
      val botVersion = v2.BotVersion(
        botId=newBot.id,
        originalFilename=oldBot.originalFilename,
        contentType=oldBot.contentType,
        sizeBytes=oldBot.sizeBytes,
        createdAt=oldBot.createdAt)

      usersTable.put(user.id, user)
      newBotsTable.put(newBot.id, newBot)
      botVersionsTable.put(botVersion.id, botVersion)
      println(s"Migrated $oldBot to $newBot")
    }
  }

  def swapBotIdToBotVersionIdInBattlesTable(db: DB) = {
    import se.ramn.models.{v1, v2}
    import se.ramn.models.Battle

    val botsTable = db.getTreeMap[UUID, v2.Bot](DbTable.bots.toString).asScala
    val botVersionsTable =
      db.getTreeMap[UUID, v2.BotVersion](DbTable.botVersions.toString).asScala
    val battlesTable =
      db.getTreeMap[UUID, Battle](DbTable.battles.toString).asScala


    def latestVersionFor(botId: UUID) = {
      import se.ramn.Joda.dateTimeOrdering
      botVersionsTable
        .values.toSeq
        .filter(_.botId == botId)
        .sortBy(_.createdAt)
        .map(_.id)
        .lastOption
    }
    val newBattlesById = battlesTable map { case (battleId, battle) =>
      val botIds = battle.botVersionIds
      val botVersionIds = botIds.flatMap(latestVersionFor)
      require(botVersionIds.size == battle.botVersionIds.size)
      (battleId, battle.copy(botVersionIds=botVersionIds))
    }
    newBattlesById foreach { case (battleId, battle) =>
      battlesTable.put(battleId, battle)
    }
  }

  def renameJarsOnDisk(db: DB) = {
    import java.nio.file.Files
    import se.ramn.models.{v1, v2}
    import se.ramn.Config

    val botsTable = db.getTreeMap[UUID, v2.Bot](DbTable.bots.toString).asScala
    val botVersionsTable =
      db.getTreeMap[UUID, v2.BotVersion](DbTable.botVersions.toString).asScala

    botVersionsTable.foreach { case (id, botVersion) =>
      val src = Config.BotDir.toPath.resolve(botVersion.botId + ".jar")
      val dest = botVersion.persistedPath
      Files.move(src, dest)
    }
  }
}


object MigrationRunner {

  def run() = {
    println("Running migrations ...")

    val nonExecuted = Db.inTx { db =>
      Migrations.migrations.keySet -- table(db).filter(isExecuted).keySet
    }

    nonExecuted foreach { migrationId =>
      Db.inTx { db =>
        println(s"Executing migration $migrationId ...")
        Migrations.migrations(migrationId)(db)
        table(db).put(migrationId, true)
        println(s"Migration $migrationId executed successfully")
      }
    }
  }

  private def isExecuted(entry: (Int, Boolean)) = {
    entry._2
  }

  private def table(db: DB) =
    db.getTreeMap[Int, Boolean](DbTable.migrations.toString).asScala
}
