package se.ramn.models

import java.util.UUID
import collection.JavaConverters._
import collection.immutable.Set

import org.mapdb.DB


package v1 {
  object UserRepository {
    def get(id: UUID): Option[User] = {
      Db.inTx { db =>
        table(db).get(id)
      }
    }

    def all: Set[User] = Db.inTx { db =>
      table(db).values.toSet
    }

    def put(bot: User) = {
      Db.inTx { db =>
        table(db).put(bot.id, bot)
      }
    }

    private def table(db: DB) =
      db.getTreeMap[UUID, User](DbTable.users.toString).asScala
  }
}
