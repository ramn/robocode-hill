package models

import java.io.File


case class Bot(jar: File) {
  def name = jar.getName
}
