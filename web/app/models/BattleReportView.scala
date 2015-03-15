package se.ramn.models

import robocode.control.events.BattleCompletedEvent
import robocode.control.BattleSpecification
import robocode.BattleResults


case class ScoreRecord(bot: Bot, battleResults: BattleResults)


class BattleReportView(report: SuccessfulBattle) {
  val SuccessfulBattle(
    battleRequest,
    RobocodeBattleReport(completedEvent, battleSpecification)
  ) = report

  private val indexedResults = completedEvent.getIndexedResults

  require(battleRequest.bots.size == indexedResults.size)

  val scoreRecordsByScore =
    (battleRequest.bots zip indexedResults)
    .map(ScoreRecord.tupled)
    .sortBy(_.battleResults.getScore * -1)
}
