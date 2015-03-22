package se

package object ramn {
  implicit val dateTimeOrdering = se.ramn.Joda.dateTimeOrdering
}
