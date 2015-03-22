package se.ramn.migrations

import java.util.UUID
import scala.util.control.NonFatal
import collection.JavaConverters._

import org.mapdb.DB

import se.ramn.models.Db
import se.ramn.models.DbTable
import se.ramn.models.{v1, v2}
import se.ramn.models.Battle
import se.ramn.Joda.dateTimeOrdering


object v2_swapBotIdToBotVersionIdInBattlesTable extends (DB => Unit) {
  def apply(db: DB) = {
    val botsTable = db.getTreeMap[UUID, v2.Bot](DbTable.bots.toString).asScala
    val botVersionsTable =
      db.getTreeMap[UUID, v2.BotVersion](DbTable.botVersions.toString).asScala
    val battlesTable =
      db.getTreeMap[UUID, Battle](DbTable.battles.toString).asScala


    def latestVersionFor(botId: UUID) = {
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
}
