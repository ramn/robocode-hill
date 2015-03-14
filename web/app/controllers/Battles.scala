package controllers

import play.api._
import play.api.mvc._
import se.ramn.roborunner.RandomBattleground
import se.ramn.models.BattleReport
import se.ramn.models.SuccessfulBattle
import se.ramn.models.FailedBattle


object Battles extends Controller {
  def index = Action {
    Ok(views.html.battles.index())
  }

  def create = Action { request =>
    val battleReport = RandomBattleground.run
    battleReport match {
      case battleReport: SuccessfulBattle =>
        Ok(views.html.battles.show(battleReport))
      case failedBattle: FailedBattle =>
        InternalServerError("500 - Failed to run battle")
    }
  }
}
