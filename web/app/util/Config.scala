package se.ramn

import java.nio.file.Paths
import java.io.File


object Config {
  private final val DataDir = Paths.get(sys.env("PWD")).resolve(sys.env("DATA_DIR"))
  private final val DbDir: File = DataDir.resolve("db").toFile

  final val DbFile: File = DbDir.toPath.resolve("db").toFile
  final val BotDir: File = DataDir.resolve("bots").toFile

  DbDir.mkdirs
  BotDir.mkdirs

  require { !DbFile.isDirectory && BotDir.isDirectory }
}
