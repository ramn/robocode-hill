package se.ramn
package models

import collection.immutable.Seq
import robocode.control.BattleSpecification


sealed trait BattleReport


case class SuccessfulBattle(
	battleRequest: BattleRequest,
	battleSpecification: BattleSpecification,
	robotBattleResults: Seq[RobotBattleResult]
) extends BattleReport


case class FailedBattle(battleRequest: BattleRequest) extends BattleReport
