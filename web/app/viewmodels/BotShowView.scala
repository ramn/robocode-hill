package se.ramn.viewmodels

import se.ramn.models.v2.Bot
import se.ramn.models.v2.BotVersion
import se.ramn.models.v1.User


case class BotShowView(
  bot: Bot,
  owner: User,
  latestVersionOpt: Option[BotVersion],
  versions: Seq[BotVersion]
)
