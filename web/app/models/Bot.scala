package se.ramn
package models

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import scala.util.control.NonFatal
import java.io.File
import java.util.UUID
import java.nio.file.Paths
import java.nio.file.Files

import org.joda.time.DateTime
import org.joda.time.DateTimeZone


case class Bot(
  id: UUID = UUID.randomUUID,
  originalFilename: String,
  contentType: String,
  sizeBytes: Long,
  createdAt: DateTime = DateTime.now(DateTimeZone.UTC)
  ) {
  def persistedFilename: String = id.toString + ".jar"
}


class UploadBotRequest(
  filename: String,
  contentType: String,
  jarfile: File
) {
  def persist(): Either[String, Bot] = {
    val bot = Bot(
      originalFilename=filename,
      contentType=contentType,
      sizeBytes=jarfile.length)

    Try {
      val target = Config.BotDir.toPath.resolve(bot.persistedFilename)
      Files.move(jarfile.toPath, target)
      BotRepository.put(bot)
      println(s"Successfully uploaded $bot")
    } match {
      case Success(_) => Right(bot)
      case Failure(e) =>
        e.printStackTrace
        Left("Error saving bot")
    }
  }
}
