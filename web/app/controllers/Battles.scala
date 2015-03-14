package controllers

import play.api._
import play.api.mvc._
import se.ramn.roborunner.RandomBattleground


object Battles extends Controller {
  def index = Action {
    val battleReportOpt = RandomBattleground.run
    battleReportOpt match {
      case Some(battleReport) =>
        Ok(views.html.battles.show(battleReport))
      case None =>
        InternalServerError("500 - Failed to run battle")
    }
  }
}
