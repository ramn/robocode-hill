package se.ramn.viewmodels

import collection.immutable.Seq
import se.ramn.models.v2.Bot
import se.ramn.models.v1.User


case class BotIndexView(botViews: Seq[BotIndexView.BotView])


object BotIndexView {
  case class BotView(bot: Bot, owner: User)
}
