package se.ramn
package models

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import scala.util.control.NonFatal
import java.io.File
import java.util.UUID
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

import se.ramn.Joda.dateTimeOrdering


// Deprecated, migrate and then remove
case class Bot(
  id: UUID = UUID.randomUUID,
  originalFilename: String,
  contentType: String,
  sizeBytes: Long,
  createdAt: DateTime = DateTime.now(DateTimeZone.UTC)
) {
  def persistedFilename: String = id.toString + ".jar"
  def persistedPath: Path = Config.BotDir.toPath.resolve(persistedFilename)
}


package v2 {
  case class Bot(
    id: UUID = UUID.randomUUID,
    name: String,
    ownerId: UUID,
    createdAt: DateTime = DateTime.now(DateTimeZone.UTC),
    modifiedAt: DateTime = DateTime.now(DateTimeZone.UTC)
  )


  case class BotVersion(
    id: UUID = UUID.randomUUID,
    botId: UUID,
    originalFilename: String,
    contentType: String,
    sizeBytes: Long,
    createdAt: DateTime = DateTime.now(DateTimeZone.UTC),
    modifiedAt: DateTime = DateTime.now(DateTimeZone.UTC)
  ) {
    def persistedFilename: String = id.toString + ".jar"
    def persistedPath: Path = Config.BotDir.toPath.resolve(persistedFilename)
    def bot: Bot = {
      BotRepository.get(botId).get
    }
  }
}


class UploadBotRequest(
  botInit: v2.Bot,
  filename: String,
  contentType: String,
  jarfile: File
) {
  def persist(): Either[String, v2.BotVersion] = {
    val bot = botInit.copy(modifiedAt=DateTime.now(DateTimeZone.UTC))
    val botVersion = v2.BotVersion(
      botId=bot.id,
      originalFilename=filename,
      contentType=contentType,
      sizeBytes=jarfile.length)

    Try {
      val target = Config.BotDir.toPath.resolve(botVersion.persistedFilename)
      Files.move(jarfile.toPath, target)
      v2.BotVersionRepository.put(botVersion)
      v2.BotRepository.put(bot)
      println(s"Successfully uploaded new version: $botVersion for bot: $bot")
    } match {
      case Success(_) =>
        Right(botVersion)
      case Failure(e) =>
        e.printStackTrace
        Left("Error saving bot")
    }
  }
}
