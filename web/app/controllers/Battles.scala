package controllers

import play.api._
import play.api.mvc._
import collection.immutable.Seq
import org.joda.time.DateTime

import se.ramn.roborunner.RandomBattleground
import se.ramn.roborunner.LastModifiedBattleground
import se.ramn.models.Battle
import se.ramn.models.BattleReport
import se.ramn.models.BattleRepository
import se.ramn.models.FailedBattle
import se.ramn.models.SuccessfulBattle
import se.ramn.viewmodels.BattleReportView
import se.ramn.Joda.dateTimeOrdering


object Battles extends Controller {
  def index = Action {
    val battles = BattleRepository.all.toIndexedSeq
      .sortBy(_.createdAt)(dateTimeOrdering.reverse)
      .take(10)
    val battleViews = battles.map(BattleReportView.apply)
    Ok(views.html.battles.index(battleViews))
  }

  def create = Action { request =>
    val battleground = requestedSelectionStrategy(request) match {
      case Some("random") => RandomBattleground
      case Some("lastModified") => LastModifiedBattleground
      case _ => RandomBattleground
    }
    println(s"Executing battle using battleground $battleground")
    val battleReport = battleground.run()
    responseFromBattleReport(battleReport)
  }

  private def requestedSelectionStrategy(
    request: Request[AnyContent]
  ): Option[String] = {
    val payload = request.body.asFormUrlEncoded
    payload.get("selectionStrategy").headOption
  }

  private def responseFromBattleReport(battleReport: BattleReport) = {
    battleReport match {
      case successfulBattle: SuccessfulBattle =>
        val battle = persistBattle(successfulBattle)
        val battleReport = BattleReportView(battle)
        Ok(views.html.battles.show(battleReport))
      case failedBattle: FailedBattle =>
        InternalServerError("500 - Failed to run battle")
    }
  }

  private def persistBattle(successfulBattle: SuccessfulBattle): Battle = {
    val SuccessfulBattle(
      request,
      specification,
      robotResults
    ) = successfulBattle
    val battle = Battle(
      botVersionIds=request.botVersions.map(_.id),
      specification=specification,
      robotResults=robotResults)
    BattleRepository.put(battle)
    battle
  }
}
