import play.api._

import se.ramn.Config


object Global extends GlobalSettings {
  override def onStart(app: Application): Unit = {
    Logger.info("Application onStart callback")
    Logger.info(s"Using DbFile: ${Config.DbFile}")
    Logger.info(s"Using BotDir: ${Config.BotDir}")
  }

  override def onStop(app: Application): Unit = {
    Logger.info("Application onStop callback")
  }
}
