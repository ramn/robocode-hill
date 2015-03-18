package se.ramn.viewmodels

import scala.collection.immutable.Seq
import se.ramn.models.v2.BotVersion
import se.ramn.models.RobotBattleResult


case class ScoreRecord(
  botVersion: BotVersion,
  battleResult: RobotBattleResult)
