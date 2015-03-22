package se.ramn.models
package v2

import java.util.UUID
import collection.JavaConverters._
import collection.immutable.Set
import collection.immutable.Seq

import org.mapdb.DB

import se.ramn.Joda.dateTimeOrdering


object BotVersionRepository {
  def get(id: UUID): Option[BotVersion] = {
    Db.inTx { db =>
      table(db).get(id)
    }
  }

  def forBotByCreatedAt(botId: UUID): Seq[BotVersion] = {
    BotVersionRepository.all
      .filter(_.botId == botId)
      .toIndexedSeq
      .sortBy(_.createdAt)
  }

  def lastVersionForBot(botId: UUID): Option[BotVersion] = {
    forBotByCreatedAt(botId).lastOption
  }

  def all: Set[BotVersion] = Db.inTx { db =>
    table(db).values.toSet
  }

  def put(bot: BotVersion) = {
    Db.inTx { db =>
      table(db).put(bot.id, bot)
    }
  }

  private def table(db: DB) =
    db.getTreeMap[UUID, BotVersion](DbTable.botVersions.toString).asScala
}
