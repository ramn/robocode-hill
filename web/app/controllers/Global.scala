import play.api._

import se.ramn.Config
import se.ramn.migrations.MigrationRunner


object Global extends GlobalSettings {
  override def onStart(app: Application): Unit = {
    Logger.info("Application onStart callback")
    Logger.info(s"Using DbFile: ${Config.DbFile}")
    Logger.info(s"Using BotDir: ${Config.BotDir}")

    MigrationRunner.run()
  }

  override def onStop(app: Application): Unit = {
    Logger.info("Application onStop callback")
  }
}
