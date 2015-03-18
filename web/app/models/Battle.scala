package se.ramn
package models

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import scala.util.control.NonFatal
import scala.collection.immutable.Seq
import java.util.UUID

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import robocode.control.BattleSpecification


case class Battle(
  id: UUID = UUID.randomUUID,
  botIds: Seq[UUID],
  robotBattleResults: Seq[RobotBattleResult],
  battleSpecification: BattleSpecification,
  createdAt: DateTime = DateTime.now(DateTimeZone.UTC)
)
