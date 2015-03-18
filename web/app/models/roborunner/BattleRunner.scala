package se.ramn.roborunner

import java.io.File
import java.nio.file.Path
import collection.immutable.Seq

import robocode.control.RobocodeEngine
import robocode.control.BattlefieldSpecification
import robocode.control.BattleSpecification
import robocode.control.RobocodeEngine

import se.ramn.models.RobocodeBattleReport


class BattleRunner(
  workingDir: File,
  botClassnames: Seq[String] = Seq("sample.RamFire", "sample.Corners")
) {
  def this(workingDir: String) = this(new File(workingDir))
  def this(workingDir: Path) = this(workingDir.toAbsolutePath.normalize.toFile)

  def run(): Option[RobocodeBattleReport] = {
    RobocodeEngine.setLogMessagesEnabled(false) // Disables Robocode logging
    val engine = new RobocodeEngine(workingDir)

    // Add our own battle listener to the RobocodeEngine
    val battleObserver = new BattleObserver
    engine.addBattleListener(battleObserver)

    engine.setVisible(false) // Don't invoke UI

    // Setup the battle specification
    val numberOfRounds = 5
    val battlefield = new BattlefieldSpecification(800, 600)
    val selectedRobots =
      engine.getLocalRepository(botSelectionSpecification(botClassnames))
    val battleSpec = new BattleSpecification(
      numberOfRounds,
      battlefield,
      selectedRobots
    )

    engine.runBattle(battleSpec, true) // waits till the battle finishes
    engine.close() // Cleanup our RobocodeEngine

    battleObserver.battleCompletedEventOpt map { completedEvent =>
      RobocodeBattleReport(completedEvent, battleSpec)
    }
  }

  private def botSelectionSpecification(botClassnames: Seq[String]): String = {
    def needsWildcardVersion(botClass: String) =
      !(botClass.startsWith("sample") || botClass.startsWith("tested."))
    def amendClassName(botClass: String) = {
      if (needsWildcardVersion(botClass))
        botClass + "*"
      else
        botClass
    }
    botClassnames.map(amendClassName).mkString(",")
  }
}
