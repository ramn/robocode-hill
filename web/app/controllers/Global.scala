import play.api._


object Global extends GlobalSettings {
  override def onStart(app: Application): Unit = {
    Logger.info("Application onStart callback")
  }

  override def onStop(app: Application): Unit = {
    Logger.info("Application onStop callback")
  }
}
