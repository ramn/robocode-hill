package se.ramn

import java.io.File

import robocode.control._
import robocode.control.events._

//import robocode.control.RobocodeEngine
//import robocode.control.events.IBattleListener


object BattleRunner {
  def main(args: Array[String]) = {
    val workingDir = if (args.length > 0) args(0) else "./sandbox"
    System.setProperty("NOSECURITY", "true")
    System.setProperty("debug", "true")
    val battleRunner = new BattleRunner(new File(workingDir))
    battleRunner.run()
    System.exit(0)
  }
}

class BattleRunner(workingDir: File) {
  def run() = {
    // Disable log messages from Robocode
    RobocodeEngine.setLogMessagesEnabled(false)
    val engine = new RobocodeEngine(workingDir)

    // Add our own battle listener to the RobocodeEngine 
    engine.addBattleListener(new BattleObserver)

    // Show the Robocode battle view
    engine.setVisible(false)

    // Setup the battle specification

    val numberOfRounds = 5
    val battlefield = new BattlefieldSpecification(800, 600); // 800x600
    val selectedRobots = engine.getLocalRepository("sample.RamFire,sample.Corners,sample.ScalaRobot")
    val battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots)

    // Run our specified battle and let it run till it is over
    engine.runBattle(battleSpec, true) // waits till the battle finishes

    // Cleanup our RobocodeEngine
    engine.close()
  }
}

//
// Our private battle listener for handling the battle event we are interested in.
//
class BattleObserver extends BattleAdaptor {

  // Called when the battle is completed successfully with battle results
  override def onBattleCompleted(e: BattleCompletedEvent): Unit = {
    System.out.println("-- Battle has completed --")

    // Print out the sorted results with the robot names
    System.out.println("Battle results:")

    e.getSortedResults.foreach { result =>
      System.out.println("  " + result.getTeamLeaderName + ": " + result.getScore)
    }
  }

  // Called when the game sends out an information message during the battle
  override def onBattleMessage(e: BattleMessageEvent): Unit = {
    System.out.println("Msg> " + e.getMessage)
  }

  // Called when the game sends out an error message during the battle
  override def onBattleError(e: BattleErrorEvent): Unit = {
    System.out.println("Err> " + e.getError)
  }
}

