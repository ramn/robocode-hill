package se.ramn.migrations

import java.util.UUID
import scala.util.control.NonFatal
import collection.JavaConverters._
import java.nio.file.Files

import org.mapdb.DB

import se.ramn.models.Db
import se.ramn.models.DbTable
import se.ramn.Joda.dateTimeOrdering
import se.ramn.models.{v1, v2}
import se.ramn.Config


object v3_renameJarsOnDisk extends (DB => Unit) {
  def apply(db: DB) = {
    val botsTable = db.getTreeMap[UUID, v2.Bot](DbTable.bots.toString).asScala
    val botVersionsTable =
      db.getTreeMap[UUID, v2.BotVersion](DbTable.botVersions.toString).asScala

    botVersionsTable.foreach { case (id, botVersion) =>
      val src = Config.BotDir.toPath.resolve(botVersion.botId + ".jar")
      val dest = botVersion.persistedPath
      Files.move(src, dest)
    }
  }
}
