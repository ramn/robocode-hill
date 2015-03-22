package se.ramn.migrations

import java.util.UUID
import scala.util.control.NonFatal
import org.mapdb.DB
import collection.JavaConverters._

import se.ramn.models.Db
import se.ramn.models.DbTable


object v1_migrateBotV1toV2 extends (DB => Unit) {
  def apply(db: DB) = {
    println("turned off")
//    import se.ramn.models.{v1, v2}
//    import se.ramn.models.Bot
//
//    db.rename("bots", "bots_v1")
//    val oldBotsTable = db.getTreeMap[UUID, Bot]("bots_v1").asScala
//    val newBotsTable = db.getTreeMap[UUID, v2.Bot]("bots_v2").asScala
//    val botVersionsTable =
//      db.getTreeMap[UUID, v2.BotVersion](DbTable.botVersions.toString).asScala
//    val usersTable =
//      db.getTreeMap[UUID, v1.User](DbTable.users.toString).asScala
//
//    oldBotsTable foreach { case (oldBotId, oldBot) =>
//      val user = v1.User(name=oldBot.originalFilename)
//      val newBot = v2.Bot(
//        id=oldBot.id,
//        name=oldBot.originalFilename,
//        ownerId=user.id,
//        createdAt=oldBot.createdAt)
//      val botVersion = v2.BotVersion(
//        botId=newBot.id,
//        originalFilename=oldBot.originalFilename,
//        contentType=oldBot.contentType,
//        sizeBytes=oldBot.sizeBytes,
//        createdAt=oldBot.createdAt)
//
//      usersTable.put(user.id, user)
//      newBotsTable.put(newBot.id, newBot)
//      botVersionsTable.put(botVersion.id, botVersion)
//      println(s"Migrated $oldBot to $newBot")
//    }
  }
}
