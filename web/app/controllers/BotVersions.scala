package controllers

import java.io.File
import java.io.FileFilter
import java.util.UUID
import collection.immutable.Seq
import scala.util.Try
import scala.util.Either.RightProjection

//import play.api._
import play.api.mvc._
import play.api.mvc.MultipartFormData
import play.api.libs.Files.TemporaryFile

import se.ramn.models.v2.Bot
import se.ramn.models.v2.BotVersion
import se.ramn.models.v2.BotRepository
import se.ramn.models.v2.BotVersionRepository
import se.ramn.models.UploadBotRequest


object BotVersions extends Controller {

  def show(id: String) = Action {
    val successfulResponse = for {
      uuid <- Try(UUID.fromString(id)).toOption
      botVersion <- BotVersionRepository.get(uuid)
      bot <- BotRepository.get(botVersion.botId)
    } yield Ok(views.html.botversions.show(bot, botVersion))
    successfulResponse.getOrElse(NotFound("404 Not Found"))
  }

  def add(botId: String) = Action { request =>
    val resultEither: Either[String, Result] = for {
      botUUID <- str2uuid(botId)
      bot <- loadBot(botUUID)
    } yield Ok(views.html.botversions.add(bot, request.flash.get("error")))
    resultEither.left.map(BadRequest(_)).merge
  }

  def create(botId: String) = Action(parse.multipartFormData) { request =>
    val result: Either[String, BotVersion] = for {
      botUUID <- str2uuid(botId)
      bot <- loadBot(botUUID)
      botVersion <- extractAndSaveBotVersion(bot, request).right
    } yield botVersion

    result match {
      case Left(error) =>
        Redirect(routes.BotVersions.add(botId)).flashing("error" -> error)
      case Right(botVersion) =>
        Redirect(routes.BotVersions.show(botVersion.id.toString))
    }
  }

  def download(id: String) = Action {
    val botVersionOpt = BotVersionRepository.get(UUID.fromString(id))
    botVersionOpt match {
      case Some(botVersion) =>
        val oneYearInSecs = 31536000
        Ok.sendFile(botVersion.persistedPath.toFile)
          .withHeaders(
            CACHE_CONTROL -> s"max-age=$oneYearInSecs",
            CONTENT_DISPOSITION -> s""""attachment; filename="${botVersion.originalFilename}"""",
            CONTENT_TYPE -> "application/java-archive")
      case None => NotFound
    }
  }

  private def str2uuid(id: String): RightProjection[String, UUID] = {
    Try(UUID.fromString(id)).toOption.toRight("Not a valid uuid").right
  }

  private def loadBot(id: UUID): RightProjection[String, Bot] = {
    BotRepository.get(id).toRight("Bot not found").right
  }

  private def extractAndSaveBotVersion(
    bot: Bot,
    request: Request[MultipartFormData[TemporaryFile]]
  ): Either[String, BotVersion] = {
    val acceptableMime = "application/java-archive"

    request.body.file("bot").map { botVersion =>
      val filename = botVersion.filename
      val contentType = botVersion.contentType.getOrElse("")
      if (contentType != acceptableMime) {
        Left("Must be a .jar file")
      } else {
        val TemporaryFile(botfile: File) = botVersion.ref

        val persistedBotResult = new UploadBotRequest(
          botInit=bot,
          filename=filename,
          contentType=contentType,
          jarfile=botfile
        ).persist()

        persistedBotResult
      }
    } getOrElse {
      Left("Must supply a bot file")
    }
  }
}
