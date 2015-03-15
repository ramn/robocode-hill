package se.ramn.models

import java.util.UUID
import collection.JavaConverters._
import collection.immutable.Set

import org.mapdb.DB


object BattleRepository {
  def get(id: UUID): Option[Battle] = {
    Db.inTx { db =>
      table(db).get(id)
    }
  }

  def all: Set[Battle] = Db.inTx { db =>
    table(db).values.toSet
  }

  def put(battle: Battle) = {
    Db.inTx { db =>
      table(db).put(battle.id, battle)
      ()
    }
  }

  private def table(db: DB) =
    db.getTreeMap[UUID, Battle](DbTable.battles.toString).asScala
}
