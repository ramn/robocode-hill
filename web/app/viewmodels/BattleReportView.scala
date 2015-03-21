package se.ramn.viewmodels

import java.util.UUID
import scala.collection.immutable.Seq

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

import se.ramn.models.Battle
import se.ramn.models.BattleRequest
import se.ramn.models.BattleSpecification
import se.ramn.models.BotRepository
import se.ramn.models.RobotBattleResult


class BattleReportView(
  val battleId: UUID,
  request: BattleRequest,
  robotResults: Seq[RobotBattleResult],
  val specification: BattleSpecification,
  val createdAt: DateTime
) {
  require(request.bots.size == robotResults.size)

  val scoreRecordsByScore =
    (request.bots zip robotResults)
    .map(ScoreRecord.tupled)
    .sortBy(_.battleResult.score * -1)
}


object BattleReportView {
  def apply(battle: Battle) = {
    val request = BattleRequest(battle.botIds.flatMap(BotRepository.get))
    new BattleReportView(
      battle.id,
      request,
      battle.robotResults,
      battle.specification,
      battle.createdAt
    )
  }
}
