package se.ramn

import java.io.File
import java.nio.file.Path

import robocode.control.RobocodeEngine
import robocode.control.BattlefieldSpecification
import robocode.control.BattleSpecification
import robocode.control.RobocodeEngine
import robocode.control.events.BattleErrorEvent
import robocode.control.events.BattleMessageEvent
import robocode.control.events.BattleCompletedEvent
import robocode.control.events.BattleAdaptor


class BattleRunner(workingDir: File) {
  def this(workingDir: String) = this(new File(workingDir))
  def this(workingDir: Path) = this(workingDir.toAbsolutePath.normalize.toFile)

  def run() = {
    RobocodeEngine.setLogMessagesEnabled(false) // Disables Robocode logging
    val engine = new RobocodeEngine(workingDir)

    // Add our own battle listener to the RobocodeEngine
    engine.addBattleListener(new BattleObserver)

    engine.setVisible(false) // Don't invoke UI

    // Setup the battle specification
    val numberOfRounds = 5
    val battlefield = new BattlefieldSpecification(800, 600)
    val selectedRobots = engine.getLocalRepository("sample.RamFire,sample.Corners")
    val battleSpec = new BattleSpecification(
      numberOfRounds,
      battlefield,
      selectedRobots
    )

    engine.runBattle(battleSpec, true) // waits till the battle finishes
    engine.close() // Cleanup our RobocodeEngine
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

