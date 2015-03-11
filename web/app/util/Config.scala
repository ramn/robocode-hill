package se.ramn

import java.nio.file.Paths
import java.io.File


object Config {
  private final val DataDir = sys.env("DATA_DIR")

  final val DbDir: File = Paths.get(DataDir, "db").toFile
  final val BotDir: File = Paths.get(DataDir, "bots").toFile

  DbDir.mkdirs
  BotDir.mkdirs
}
