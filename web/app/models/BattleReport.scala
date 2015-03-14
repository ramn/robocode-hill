package se.ramn.models

import robocode.control.events.BattleCompletedEvent
import robocode.control.BattleSpecification


sealed trait BattleReport


case class SuccessfulBattle(
  battleRequest: BattleRequest,
  robocodeBattleReport: RobocodeBattleReport
) extends BattleReport


case class FailedBattle(battleRequest: BattleRequest) extends BattleReport


case class RobocodeBattleReport(
  completedEvent: BattleCompletedEvent,
  battleSpecification: BattleSpecification)
