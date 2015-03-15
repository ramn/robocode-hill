package se.ramn.models

import java.util.UUID

import robocode.control.events.BattleCompletedEvent
import robocode.control.BattleSpecification
import robocode.BattleResults
import org.joda.time.DateTime
import org.joda.time.DateTimeZone


case class ScoreRecord(bot: Bot, battleResults: BattleResults)


class BattleReportView(
  val battleId: UUID,
  battleRequest: BattleRequest,
  completedEvent: BattleCompletedEvent,
  val battleSpecification: BattleSpecification,
  val createdAt: DateTime
) {
  private val indexedResults = completedEvent.getIndexedResults
  require(battleRequest.bots.size == indexedResults.size)

  val scoreRecordsByScore =
    (battleRequest.bots zip indexedResults)
    .map(ScoreRecord.tupled)
    .sortBy(_.battleResults.getScore * -1)
}


object BattleReportView {
  def apply(battle: Battle) = {
    val battleRequest = BattleRequest(battle.botIds.flatMap(BotRepository.get))
    import battle.completedEvent
    import battle.battleSpecification
    new BattleReportView(
      battle.id,
      battleRequest,
      completedEvent,
      battleSpecification,
      battle.createdAt
    )
  }
}
