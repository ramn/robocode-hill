import NativePackagerKeys._

name := """robocode-hill"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.5"

maintainer in Docker := "ramn"

bashScriptConfigLocation := Some("${app_home}/../conf/jvmopts")

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  //jdbc,
  //anorm,
  cache,
  ws,
  "org.mapdb" % "mapdb" % "1.0.7"
  //"net.sf.robocode" % "robocode.api" % "1.9.2.1",
)
