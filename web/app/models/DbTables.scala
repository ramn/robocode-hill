package se.ramn.models


object DbTable extends Enumeration {
  type DbTable = Value

  val battles = Value("battles2") // "battles" table is broken!
  val bots = Value("bots_v2")
  val botVersions = Value("bot_versions")
  val migrations = Value("migrations")
  val users = Value("users")
}
