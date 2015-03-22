package se.ramn.models

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import java.io.File
import java.nio.file.Files

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

import se.ramn.Config
import se.ramn.models.v2.Bot
import se.ramn.models.v2.BotRepository
import se.ramn.models.v2.BotVersion
import se.ramn.models.v2.BotVersionRepository


class UploadBotRequest(
  botInit: Bot,
  filename: String,
  contentType: String,
  jarfile: File
) {
  def persist(): Either[String, BotVersion] = {
    val bot = botInit.copy(modifiedAt=DateTime.now(DateTimeZone.UTC))
    val botVersion = BotVersion(
      botId=bot.id,
      originalFilename=filename,
      contentType=contentType,
      sizeBytes=jarfile.length)

    Try {
      val target = Config.BotDir.toPath.resolve(botVersion.persistedFilename)
      Files.move(jarfile.toPath, target)
      BotVersionRepository.put(botVersion)
      BotRepository.put(bot)
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
