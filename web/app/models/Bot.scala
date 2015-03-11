package se.ramn.models

import java.io.File
import java.util.UUID


case class Bot(
  id: UUID = UUID.randomUUID,
  originalFile: File
  ) {
  def persistedFilename: String = id.toString + ".jar"
}
