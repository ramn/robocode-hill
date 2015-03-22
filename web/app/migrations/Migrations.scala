package se.ramn.migrations

import java.util.UUID
import scala.util.control.NonFatal
import org.mapdb.DB
import collection.JavaConverters._

import se.ramn.models.Db
import se.ramn.models.DbTable


object Migrations {
  val migrations: Map[Int, DB => Unit] = List(
    1 -> v1_migrateBotV1toV2,
    2 -> v2_swapBotIdToBotVersionIdInBattlesTable,
    3 -> v3_renameJarsOnDisk
  ).toMap
}


object MigrationRunner {
  def run() = {
    println("Running migrations ...")
    val nonExecuted = Db.inTx { db =>
      Migrations.migrations.keySet -- table(db).filter(isExecuted).keySet
    }

    nonExecuted foreach { migrationId =>
      try {
        Db.inTx { db =>
          println(s"Executing migration $migrationId ...")
          Migrations.migrations(migrationId)(db)
          table(db).put(migrationId, true)
          println(s"Migration $migrationId executed successfully")
        }
      } catch {
        case NonFatal(e) => e.printStackTrace
      }
    }
  }

  private def isExecuted(entry: (Int, Boolean)) = entry._2

  private def table(db: DB) =
    db.getTreeMap[Int, Boolean](DbTable.migrations.toString).asScala
}
