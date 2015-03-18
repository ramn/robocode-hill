package se.ramn.roborunner

import robocode.control.events.BattleCompletedEvent
import robocode.control.BattleSpecification


case class BattleRunnerResult(
  completedEvent: BattleCompletedEvent,
  battleSpecification: BattleSpecification)
