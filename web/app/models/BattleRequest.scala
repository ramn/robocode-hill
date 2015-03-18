package se.ramn.models

import collection.immutable.Seq

import se.ramn.models.v2.BotVersion


case class BattleRequest(botVersions: Seq[BotVersion])
