package se.ramn.models

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import scala.util.control.NonFatal
import java.io.File
import java.util.UUID
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files

import org.joda.time.DateTime
import org.joda.time.DateTimeZone


package v1 {
  case class User(
    id: UUID = UUID.randomUUID,
    name: String,
    createdAt: DateTime = DateTime.now(DateTimeZone.UTC),
    modifiedAt: DateTime = DateTime.now(DateTimeZone.UTC))
}
