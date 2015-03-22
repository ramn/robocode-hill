package controllers

import java.io.File
import java.io.FileFilter
import java.util.UUID
import collection.immutable.Seq
import scala.util.Try
import scala.util.Success
import scala.util.Failure

//import play.api._
import play.api.mvc._
import play.api.mvc.MultipartFormData
import play.api.libs.Files.TemporaryFile

import se.ramn.models.v1.User
import se.ramn.models.v1.UserRepository
import se.ramn.models.v2.Bot
import se.ramn.models.v2.BotRepository
import se.ramn.models.v2.BotVersionRepository


object Bots extends Controller {

  def index = Action {
    Ok(views.html.bots.index(listBots))
  }

  def show(id: String) = Action {
    loadBot(id).right.map { bot =>
      val versions = BotVersionRepository.forBotByCreatedAt(bot.id)
      val latestVersionOpt = versions.lastOption
      Ok(views.html.bots.show( bot, latestVersionOpt, versions.reverse))
    }.merge
  }

  //def add = Action { request =>
    //Ok(views.html.bots.add(request.flash.get("error")))
  //}

  // TODO: add form page for this
  def create = Action { request =>
    val newBotOpt: Option[Bot] = for {
      form <- request.body.asFormUrlEncoded
      ownerNames <- form.get("ownerName")
      ownerName <- ownerNames.headOption
      botNames <- form.get("botName")
      botName <- botNames.headOption
    } yield {
      val user = User(name=ownerName)
      Bot(name=botName, ownerId=user.id)
    }

    newBotOpt match {
      case Some(bot) => Redirect(routes.Bots.show(bot.id.toString))
      case None => BadRequest("Missing form data for creating new Bot")
    }
  }

  private def listBots: Iterable[Bot] = BotRepository.all

  private def loadBot(botId: String): Either[Result, Bot] = {
    Try(BotRepository.get(UUID.fromString(botId))) match {
      case Success(Some(bot)) => Right(bot)
      case Failure(e) => Left(NotFound(e.getMessage))
      case Success(None) => Left(NotFound("Bot not found"))
    }
  }
}
