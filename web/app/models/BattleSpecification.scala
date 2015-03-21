package se.ramn.models

import robocode.control


case class BattleSpecification(
  battlefieldHeight: Int,
  battlefieldWidth: Int,
  gunCoolingRate: Double,
  hideEnemyNames: Boolean,
  inactivityTime: Long,
  numRounds: Int,
  sentryBorderSize: Int)


object BattleSpecification {
  def from(spec: control.BattleSpecification) = {
    BattleSpecification(
      battlefieldHeight=spec.getBattlefield.getHeight,
      battlefieldWidth=spec.getBattlefield.getWidth,
      gunCoolingRate=spec.getGunCoolingRate,
      hideEnemyNames=spec.getHideEnemyNames,
      inactivityTime=spec.getInactivityTime,
      numRounds=spec.getNumRounds,
      sentryBorderSize=spec.getSentryBorderSize)
  }
}
