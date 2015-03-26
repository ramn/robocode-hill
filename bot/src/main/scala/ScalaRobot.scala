package se.ramn.ramnbot

import java.awt.Color
import scala.util.Random

import robocode.HitByBulletEvent
import robocode.AdvancedRobot
import robocode.ScannedRobotEvent
import robocode.util.Utils.normalRelativeAngleDegrees


class RamnBot extends AdvancedRobot {
  override def run = {
    // Can we load Scala classes? Else null pointer exception
    out.println(scala.math.BigInt(1))

    setAdjustGunForRobotTurn(true) // Keep the gun still when we turn
    setAdjustRadarForGunTurn(true)
    setAdjustRadarForRobotTurn(true)

    setColors(
      new Color(0, 100, 255),
      new Color(255, 0, 0),
      new Color(0, 0, 0))

    while (true) {
      execute()
      setTurnRadarRight(360)
      setTurnRight(360)
      setAhead(10000)
      setFire(1)
    }
  }

  override def onScannedRobot(e: ScannedRobotEvent) = {
		fire(1)
	}

  override def onHitByBullet(e: HitByBulletEvent) = {
    turnLeft(90 - e.getBearing)
  }
}
