package se.ramn

import org.joda.time.DateTime
import org.joda.time.DateTimeZone


object Joda {
  implicit def dateTimeOrdering: Ordering[DateTime] = Ordering.fromLessThan(_ isBefore _)
}
