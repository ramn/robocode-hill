package se.ramn.models

import java.util.UUID
import collection.JavaConverters._
import collection.immutable.Set

import org.mapdb.DB


package v1 {
  object BotRepository {
    def get(id: UUID): Option[Bot] = {
      Db.inTx { db =>
        table(db).get(id)
      }
    }

    def all: Set[Bot] = Db.inTx { db =>
      table(db).values.toSet
    }

    def put(bot: Bot) = {
      Db.inTx { db =>
        table(db).put(bot.id, bot)
      }
    }

    private def table(db: DB) =
      db.getTreeMap[UUID, Bot](DbTable.bots.toString).asScala
  }
}


package v2 {
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


  object BotVersionRepository {
    def get(id: UUID): Option[BotVersion] = {
      Db.inTx { db =>
        table(db).get(id)
      }
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
}
