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
import se.ramn.viewmodels.BotShowView
import se.ramn.viewmodels.BotIndexView
import se.ramn.Joda.dateTimeOrdering


object Bots extends Controller {

  def index = Action {
    Ok(views.html.bots.index(botIndexView))
  }

  def show(id: String) = Action {
    val resultEither = for {
      bot <- loadBot(id).right
      owner <- loadOwner(bot).right
    } yield {
      val versions = BotVersionRepository
        .forBotByCreatedAt(bot.id)
        .sortBy(_.createdAt)(dateTimeOrdering.reverse)
      val latestVersionOpt = versions.lastOption
      val botShowView = BotShowView(bot, owner, latestVersionOpt, versions)
      Ok(views.html.bots.show(botShowView))
    }
    resultEither.merge
  }

  def add = Action { request =>
    Ok(views.html.bots.add(request.flash.get("error")))
  }

  def create = Action { request =>
    val newBotOpt: Option[Bot] = for {
      form <- request.body.asFormUrlEncoded
      ownerNames <- form.get("ownerName")
      ownerName <- ownerNames.headOption
      botNames <- form.get("botName")
      botName <- botNames.headOption
    } yield {
      val newUser = User(name=ownerName)
      val user = UserRepository.putIfAbsent(newUser).getOrElse(newUser)
      val bot = Bot(name=botName, ownerId=user.id)
      BotRepository.put(bot)
      bot
    }

    newBotOpt match {
      case Some(bot) => Redirect(routes.Bots.show(bot.id.toString))
      case None => BadRequest("Missing form data for creating new Bot")
    }
  }

  private def botIndexView = {
    val bots = listBots
      .toIndexedSeq
      .sortBy(_.modifiedAt)(dateTimeOrdering.reverse)
    val botViews = for {
      bot <- bots
      owner <- UserRepository.get(bot.ownerId)
    } yield BotIndexView.BotView(bot, owner)
    BotIndexView(botViews)
  }

  private def listBots: Iterable[Bot] = BotRepository.all

  private def loadOwner(bot: Bot) = {
    UserRepository.get(bot.ownerId)
      .toRight(NotFound("Bot owner not found"))
  }

  private def loadBot(botId: String): Either[Result, Bot] = {
    Try(BotRepository.get(UUID.fromString(botId))) match {
      case Success(Some(bot)) => Right(bot)
      case Failure(e) => Left(NotFound(e.getMessage))
      case Success(None) => Left(NotFound("Bot not found"))
    }
  }
}
