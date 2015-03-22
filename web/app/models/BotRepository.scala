package se.ramn.models
package v2

import java.util.UUID
import collection.JavaConverters._
import collection.immutable.Set

import org.mapdb.DB


object BotRepository {
  def get(id: UUID): Option[Bot] = {
    Db.inTx { db =>
      table(db).get(id)
    }
  }

  def all: Set[Bot] = Db.inTx { db =>
    table(db).values.toSet
  }

  def put(bot: Bot): Unit = {
    Db.inTx { db =>
      table(db).put(bot.id, bot)
    }
    ()
  }

  private def table(db: DB) =
    db.getTreeMap[UUID, Bot](DbTable.bots.toString).asScala
}
