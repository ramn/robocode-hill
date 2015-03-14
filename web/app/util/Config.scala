package se.ramn

import java.nio.file.Paths
import java.io.File


object Config {
  private val dataDirRelative = sys.env.get("DATA_DIR").getOrElse("tmp/data")
  private val DataDir = Paths.get(sys.env("PWD")).resolve(dataDirRelative)
  private val DbDir: File = DataDir.resolve("db").toFile

  final val DbFile: File = DbDir.toPath.resolve("db").toFile
  final val BotDir: File = DataDir.resolve("bots").toFile

  DbDir.mkdirs
  BotDir.mkdirs

  require { !DbFile.isDirectory && BotDir.isDirectory }
}
