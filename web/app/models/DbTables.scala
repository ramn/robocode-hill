package se.ramn.models


object DbTable extends Enumeration {
  type DbTable = Value
  val bots = Value("bots")
  val battles = Value("battles")
}
