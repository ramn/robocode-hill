package controllers

import java.nio.file.Paths
import play.api._
import play.api.mvc._
import se.ramn.BattleRunner


object Application extends Controller {
  def index = Action {
    Redirect("/bots")
  }

  def battle = Action {
    // TODO: run Battle in temp dir
    val workingDir = Paths.get(sys.env("PWD"), "..", "sandbox")
    val battleRunner = new BattleRunner(workingDir)
    battleRunner.run()
    Ok("Battle done")
  }
}
