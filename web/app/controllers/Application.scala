package controllers

import play.api._
import play.api.mvc._
import se.ramn.robocode.RandomBattleground


object Application extends Controller {
  def index = Action {
    Redirect("/bots")
  }

  def battle = Action {
    RandomBattleground.run
    Ok("Battle done")
  }
}
