package se.ramn

import java.io.File
import java.io.FileOutputStream
import java.io.BufferedInputStream
import java.nio.file.Files
import java.util.zip.ZipFile
import java.util.zip.ZipEntry
import java.util.jar.JarFile
import java.util.jar.JarEntry
import java.util.Properties
import scala.util.Try
import scala.util.control.NonFatal
import scala.collection.JavaConverters._
import scala.collection.immutable.Seq


object JarExtractor {
  def deflate(file: File, outputDir: File, delegate: JarExtractorDelegate): Unit = {
    val jarFile = new JarFile(file)
    jarFile
      .entries.asScala
      .filter { delegate.entryFound(_, jarFile) }
      .foreach { processEntry(_, jarFile, outputDir) }
  }

  private def processEntry(entry: JarEntry, jarFile: JarFile, outputDir: File) = {
    println(s"extracting file: ${entry.getName}")
    val entryDestination = new File(outputDir, entry.getName)
    entryDestination.getParentFile.mkdirs()
    if (entry.isDirectory) {
      entryDestination.mkdirs()
    } else {
      val in = new BufferedInputStream(jarFile.getInputStream(entry))
      Files.copy(in, entryDestination.toPath)
      try in.close() catch { case NonFatal(e) => }
    }
  }
}


trait JarExtractorDelegate {
  /*
   * Called for each entry found in the Jar file, before that entry is being
   * extracted.
   *
   * If this method returns false the entry is not extracted.
   */
  def entryFound(entry: JarEntry, jarFile: JarFile): Boolean
}


class BotExtractorDelegate extends JarExtractorDelegate {
  private var myFilesFound = Seq.empty[String]
  private var myPropertiesOpt: Option[Properties] = None

  def filesFound = myFilesFound
  def propertiesOpt = myPropertiesOpt

  override def entryFound(entry: JarEntry, jarFile: JarFile): Boolean = {
    val entryname = entry.getName
    if (!entry.isDirectory) {
      myFilesFound :+= entryname
    }
    if (entryname.endsWith(".properties")) {
      try {
        val properties = new Properties()
        properties.load(jarFile.getInputStream(entry))
        myPropertiesOpt = Some(properties)
      } catch {
        case e: Exception => e.printStackTrace
      }
    }
    val doExtract =
      !entryname.startsWith("META-INF")
      //&& !entryname.endsWith(".properties")
    doExtract
  }
}
