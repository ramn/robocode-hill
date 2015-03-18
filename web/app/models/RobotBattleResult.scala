package se.ramn.models

import scala.collection.immutable.Seq
import robocode.control.events.BattleCompletedEvent


case class RobotBattleResult(
  bulletDamage: Int,
  bulletDamageBonus: Int,
  firsts: Int,
  lastSurvivorBonus: Int,
  ramDamage: Int,
  ramDamageBonus: Int,
  rank: Int,
  score: Int,
  seconds: Int,
  survival: Int,
  teamLeaderName: String,
  thirds: Int
)


object RobotBattleResult {
  def from(event: BattleCompletedEvent): Seq[RobotBattleResult] = {
    event.getIndexedResults.toIndexedSeq.map { result =>
      RobotBattleResult(
        bulletDamage=result.getBulletDamage,
        bulletDamageBonus=result.getBulletDamageBonus,
        firsts=result.getFirsts,
        lastSurvivorBonus=result.getLastSurvivorBonus,
        ramDamage=result.getRamDamage,
        ramDamageBonus=result.getRamDamageBonus,
        rank=result.getRank,
        score=result.getScore,
        seconds=result.getSeconds,
        survival=result.getSurvival,
        teamLeaderName=result.getTeamLeaderName,
        thirds=result.getThirds)
    }
  }
}
