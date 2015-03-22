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

    def findByName(name: String): Option[User] = {
      all.filter(_.name == name).headOption
    }

    def all: Set[User] = Db.inTx { db =>
      table(db).values.toSet
    }

    def put(user: User): Unit = {
      Db.inTx { db =>
        table(db).put(user.id, user)
      }
      ()
    }

    def putIfAbsent(user: User): Option[User] = {
      Db.inTx { db =>
        table(db).putIfAbsent(user.id, user)
      }
    }

    private def table(db: DB) =
      db.getTreeMap[UUID, User](DbTable.users.toString).asScala
  }
}
