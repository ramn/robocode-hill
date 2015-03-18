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


case class Battle(
  id: UUID = UUID.randomUUID,
  botVersionIds: Seq[UUID],
  robotResults: Seq[RobotBattleResult],
  specification: BattleSpecification,
  createdAt: DateTime = DateTime.now(DateTimeZone.UTC)
)
