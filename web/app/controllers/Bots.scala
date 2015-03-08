package controllers

import java.io.File
import java.io.FileFilter
import scala.collection.JavaConversions._
import collection.immutable.Seq

import play.api._
import play.api.mvc._

import models.Bot


object Bots extends Controller {
  val botDir = sys.env.get("ROBOT_DIR").getOrElse("/data/bots")

  def index = Action {
    Ok(views.html.bots.index(listBots))
  }

  def add = Action {
    Ok(views.html.bots.add())
  }

  def create = Action(parse.multipartFormData) { request =>
    request.body.file("bot").map { bot =>
      val filename = bot.filename
      val contentType = bot.contentType
      bot.ref.moveTo(new File(s"$botDir/$filename"))
      Ok("Bot uploaded")
      }.getOrElse {
        Redirect(routes.Bots.index).flashing(
          "error" -> "Missing file")
      }
  }

  private def listBots: Seq[Bot] = {
    class JarsFilter extends FileFilter {
      override def accept(file: File): Boolean = {
        file.getName.endsWith(".jar")
      }
    }
    val myBotDir = new File(botDir)
    if (!myBotDir.exists) {
      Seq.empty
    } else {
      val jars = myBotDir.listFiles(new JarsFilter)
      jars.to[Seq] map { botFile =>
        Bot(botFile)
      }
    }
  }
}
