package controllers

import java.io.File
import java.io.FileFilter
import java.util.UUID
import collection.immutable.Seq

//import play.api._
import play.api.mvc._
import play.api.mvc.MultipartFormData
import play.api.libs.Files.TemporaryFile

import se.ramn.models.Bot
import se.ramn.models.BotRepository
import se.ramn.models.UploadBotRequest


object Bots extends Controller {

  def index = Action {
    Ok(views.html.bots.index(listBots))
  }

  def show(id: String) = Action {
    val botOpt = BotRepository.get(UUID.fromString(id))
    botOpt match {
      case Some(bot) => Ok(views.html.bots.show(bot))
      case None => NotFound
    }
  }

  def add = Action { request =>
    Ok(views.html.bots.add(request.flash.get("error")))
  }

  def create = Action(parse.multipartFormData) { request =>
    handleBotUpload(request) match {
      case Left(error) =>
        Redirect(routes.Bots.add).flashing("error" -> error)
      case Right(bot) =>
        Redirect(routes.Bots.show(bot.id.toString))
    }
  }

  def download(id: String) = Action {
    val botOpt = BotRepository.get(UUID.fromString(id))
    botOpt match {
      case Some(bot) =>
        val oneYearInSecs = 31536000
        Ok.sendFile(bot.persistedPath.toFile, inline=true)
          .withHeaders(
            CACHE_CONTROL -> s"max-age=$oneYearInSecs",
            CONTENT_DISPOSITION -> s"attachment; filename=${bot.originalFilename}",
            CONTENT_TYPE -> "application/java-archive")
      case None => NotFound
    }
  }

  private def handleBotUpload(
    request: Request[MultipartFormData[TemporaryFile]]
  ): Either[String, Bot] = {
    val acceptableMime = "application/java-archive"

    request.body.file("bot").map { bot =>
      val filename = bot.filename
      val contentType = bot.contentType.getOrElse("")
      if (contentType != acceptableMime) {
        Left("Must be a .jar file")
      } else {
        val TemporaryFile(botfile: File) = bot.ref
        val persistedBotResult = new UploadBotRequest(
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

  private def listBots: Iterable[Bot] = BotRepository.all
}
