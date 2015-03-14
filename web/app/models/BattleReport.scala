package se.ramn.models

import robocode.control.events.BattleCompletedEvent
import robocode.control.BattleSpecification


case class BattleReport(
  completedEvent: BattleCompletedEvent,
  battleSpecification: BattleSpecification)
