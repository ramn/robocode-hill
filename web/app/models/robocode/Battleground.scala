package se.ramn
package robocode

import scala.util.Random
import scala.collection.immutable.Seq
import java.io.File
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.Files
import java.util.Properties

import se.ramn.models.Bot
import se.ramn.models.BotRepository


object RandomBattleground {
  def run() = {
    InTempDir { tempdir =>
      val botSelection = generateBotSelection
      val robotdir = new File(tempdir, "robots")
      val mainClasses = botSelection map { bot =>
        unpackBotInSandbox(bot, robotdir)
      }
      val battleRunner = new BattleRunner(tempdir, mainClasses.toSet)
      battleRunner.run()
    }
  }

  private def generateBotSelection: Seq[Bot] = {
    val indexedBots = BotRepository.all.toIndexedSeq
    val count = indexedBots.size
    Random.shuffle((0 until count).toIndexedSeq)
      .take(2)
      .map(ix => indexedBots(ix))
      .toIndexedSeq
  }

  /*
   * Returns main class for bot
   */
  private def unpackBotInSandbox(bot: Bot, tempdir: File): String = {
    val botExtractorDelegate = new BotExtractorDelegate
    val botFile = bot.persistedPath.toFile
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