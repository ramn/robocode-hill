package se.ramn
package roborunner

import scala.util.Random
import scala.collection.immutable.Seq
import java.io.File
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files
import java.util.Properties

import se.ramn.models.v2.Bot
import se.ramn.models.v2.BotRepository
import se.ramn.models.v2.BotVersion
import se.ramn.models.v2.BotVersionRepository
import se.ramn.models.BattleRequest
import se.ramn.models.BattleReport
import se.ramn.models.SuccessfulBattle
import se.ramn.models.FailedBattle
import se.ramn.models.RobotBattleResult
import se.ramn.models.BattleSpecification


object RandomBattleground extends Battleground {
  def run(): BattleReport = {
    val botVersions = generateBotSelection.flatMap { bot =>
      BotVersionRepository.lastVersionForBot(bot.id)
    }
    val specification = BattleRequest(botVersions=botVersions)
    run(specification)
  }

  private def generateBotSelection: Seq[Bot] = {
    val indexedBots = BotRepository.all.toIndexedSeq
    val count = indexedBots.size
    Random.shuffle((0 until count).toIndexedSeq)
      .take(2)
      .map(ix => indexedBots(ix))
      .toIndexedSeq
  }
}


class Battleground {
  def run(battleRequest: BattleRequest): BattleReport = {
    InTempDir { tempdir =>
      val robotdir = new File(tempdir, "robots")
      val mainClasses = battleRequest.botVersions map { botVersion =>
        unpackBotInSandbox(botVersion, robotdir)
      }
      val battleRunner = new BattleRunner(tempdir, mainClasses)
      val battleRunnerResultOpt = battleRunner.run()
      battleRunnerResultOpt match {
        case Some(report) =>
          SuccessfulBattle(
            request=battleRequest,
            specification=BattleSpecification.from(report.battleSpecification),
            robotResults=RobotBattleResult.from(report.completedEvent))
        case None =>
          FailedBattle(battleRequest)
      }
    }
  }

  /*
   * Returns main class for bot
   */
  private def unpackBotInSandbox(
    botVersion: BotVersion,
    tempdir: File
  ): String = {
    val botExtractorDelegate = new BotExtractorDelegate
    val botFile = botVersion.persistedPath.toFile
    JarExtractor.deflate(botFile, tempdir, botExtractorDelegate)
    extractMainClassName(botExtractorDelegate)
  }

  private def extractMainClassName(
    botExtractor: BotExtractorDelegate
  ): String = {
    botExtractor.propertiesOpt
      .flatMap(mainClassFromProperties)
      .getOrElse(mainClassFromFiles(botExtractor.filesFound))
  }

  private def mainClassFromProperties(properties: Properties): Option[String] = {
    // #Robocode Robot
    // #Thu Mar 12 20:52:13 CET 2015
    // robot.description=Wrenbot
    // robot.include.source=false
    // robocode.version=1.9.2.4
    // robot.version=0.1
    // robot.author.name=Wren
    // robot.codesize=98
    // robot.classname=wren.WrenBot
    Option(properties.getProperty("robot.classname"))
  }

  private def mainClassFromFiles(files: Seq[String]): String = {
    files
      .filter { _.endsWith(".class") }
      .sortBy(_.length * -1)
      .map(toClassQualifier)
      .head
  }

  private def toClassQualifier(path: String): String = {
    path
      .stripSuffix(".class")
      .replaceAll("/", ".")
  }
}
