package se.ramn.roborunner

import robocode.control.events.BattleAdaptor
import robocode.control.events.BattleErrorEvent
import robocode.control.events.BattleMessageEvent
import robocode.control.events.BattleCompletedEvent


private class BattleObserver extends BattleAdaptor {
  private var myBattleCompletedEvent: Option[BattleCompletedEvent] = None

  def battleCompletedEventOpt: Option[BattleCompletedEvent] = myBattleCompletedEvent

  // Called when the battle is completed successfully with battle results
  override def onBattleCompleted(e: BattleCompletedEvent): Unit = {
    myBattleCompletedEvent = Some(e)

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
