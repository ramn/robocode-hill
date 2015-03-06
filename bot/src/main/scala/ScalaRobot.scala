package sample

import robocode.HitByBulletEvent
import robocode.Robot
import robocode.ScannedRobotEvent

class ScalaRobot extends Robot {
  override def run() = {
		while (true) {
			ahead(100)
			turnGunRight(360)
			back(100)
			turnGunRight(360)
		}
  }

  override def onScannedRobot(e: ScannedRobotEvent) = {
		fire(1)
	}

  override def onHitByBullet(e: HitByBulletEvent) = {
    turnLeft(90 - e.getBearing)
  }
}
