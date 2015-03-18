package se.ramn.viewmodels

import java.util.UUID
import scala.collection.immutable.Seq

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

import se.ramn.models.Battle
import se.ramn.models.BattleRequest
import se.ramn.models.BattleSpecification
import se.ramn.models.v2.BotRepository
import se.ramn.models.v2.BotVersionRepository
import se.ramn.models.RobotBattleResult


class BattleReportView(
  val battleId: UUID,
  request: BattleRequest,
  robotResults: Seq[RobotBattleResult],
  val specification: BattleSpecification,
  val createdAt: DateTime
) {
  require(
    request.botVersions.size == robotResults.size,
    s"request.botVersions.size ${request.botVersions.size} not equal to " +
    s"robotResults.size ${robotResults.size}"
    )

  val scoreRecordsByScore =
    (request.botVersions zip robotResults)
    .map(ScoreRecord.tupled)
    .sortBy(_.battleResult.score * -1)
}


object BattleReportView {
  def apply(battle: Battle) = {
    val botVersions = battle.botVersionIds.flatMap(BotVersionRepository.get)
    val request = BattleRequest(botVersions)
    new BattleReportView(
      battle.id,
      request,
      battle.robotResults,
      battle.specification,
      battle.createdAt
    )
  }
}
