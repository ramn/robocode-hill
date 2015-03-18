package se.ramn.models

import java.util.UUID
import scala.collection.immutable.Seq

import robocode.control.BattleSpecification
import org.joda.time.DateTime
import org.joda.time.DateTimeZone


class BattleReportView(
  val battleId: UUID,
  battleRequest: BattleRequest,
  robotBattleResults: Seq[RobotBattleResult],
  val battleSpecification: BattleSpecification,
  val createdAt: DateTime
) {
  require(battleRequest.bots.size == robotBattleResults.size)

  val scoreRecordsByScore =
    (battleRequest.bots zip robotBattleResults)
    .map(ScoreRecord.tupled)
    .sortBy(_.battleResult.score * -1)
}


object BattleReportView {
  def apply(battle: Battle) = {
    val battleRequest = BattleRequest(battle.botIds.flatMap(BotRepository.get))
    new BattleReportView(
      battle.id,
      battleRequest,
      battle.robotBattleResults,
      battle.battleSpecification,
      battle.createdAt
    )
  }
}
