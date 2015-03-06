import NativePackagerKeys._

name := """robocode-hill-web"""

version := "1.0-SNAPSHOT"

maintainer in Docker := "ramn"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  //jdbc,
  //anorm,
  cache,
  ws,
  "org.mapdb" % "mapdb" % "1.0.7"
  //"net.sf.robocode" % "robocode.api" % "1.9.2.1",
)
