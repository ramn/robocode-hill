package se.ramn.viewmodels

import scala.collection.immutable.Seq
import se.ramn.models.Bot
import se.ramn.models.RobotBattleResult


case class ScoreRecord(bot: Bot, battleResult: RobotBattleResult)
