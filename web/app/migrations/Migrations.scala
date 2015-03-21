package se.ramn.migrations

import java.util.UUID
import org.mapdb.DB
import collection.JavaConverters._

import se.ramn.models.Db
import se.ramn.models.DbTable


object Migrations {
  val migrations: Map[Int, DB => Unit] = Map.empty
    //(1 -> migrationScript1 _) +
    //(2 -> migrationScript2 _)

  //def migrationScript1(db: DB) = {
    //val table = db.getTreeMap("battles").asScala
  //}
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
