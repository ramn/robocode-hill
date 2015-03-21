package se.ramn
package models

import collection.immutable.Seq


sealed trait BattleReport


case class SuccessfulBattle(
	request: BattleRequest,
	specification: BattleSpecification,
	robotResults: Seq[RobotBattleResult]
) extends BattleReport


case class FailedBattle(request: BattleRequest) extends BattleReport
