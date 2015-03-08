package controllers

import java.nio.file.Paths
import play.api._
import play.api.mvc._
import se.ramn.BattleRunner


object Application extends Controller {
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def battle = Action {
    val workingDir = Paths.get(sys.env("PWD"), "..", "sandbox")
    val battleRunner = new BattleRunner(workingDir)
    battleRunner.run()

    Ok("Battle done")
  }
}
