package controllers

import java.io.File
import java.io.FileFilter
//import scala.collection.JavaConversions._
import collection.immutable.Seq

//import play.api._
import play.api.mvc._
import play.api.mvc.MultipartFormData
import play.api.libs.Files.TemporaryFile

import models.Bot


object Bots extends Controller {
  val botDir = sys.env.get("ROBOT_DIR").getOrElse("/data/bots")

  def index = Action {
    Ok(views.html.bots.index(listBots))
  }

  def add = Action { request =>
    Ok(views.html.bots.add(request.flash.get("error")))
  }

  def create = Action(parse.multipartFormData) { request =>
    handleBotUpload(request) match {
      case Left(error) =>
        Redirect(routes.Bots.add).flashing("error" -> error)
      case Right(()) =>
        Ok("Bot uploaded")
    }
  }

  private def handleBotUpload(request: Request[MultipartFormData[TemporaryFile]]) = {
    val acceptableMime = "application/java-archive"

    request.body.file("bot").map { bot =>
      val filename = bot.filename
      val contentType = bot.contentType
      if (!contentType.map(_ == acceptableMime).getOrElse(false)) {
        Left("Must be a .jar file")
      } else {
        // TODO: handle uploaded file
        //bot.ref.moveTo(new File(s"$botDir/$filename"))
        Right(())
      }
    } getOrElse {
      Left("Must supply a bot file")
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
