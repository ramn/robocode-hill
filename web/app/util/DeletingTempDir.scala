package se.ramn

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import org.apache.commons.io.FileUtils


object InTempDir {
  def apply(thunk: File => Unit): Unit = {
    val tempdir = Files.createTempDirectory("robocode-hill").toFile
    try {
      thunk(tempdir)
    } finally {
      FileUtils.deleteDirectory(tempdir)
    }
  }
}
