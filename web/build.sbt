import NativePackagerKeys._

name := """robocode-hill"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.5"

maintainer in Docker := "ramn"

bashScriptConfigLocation := Some("${app_home}/../conf/jvmopts")

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalacOptions ++= Seq(
  "-feature",
  "-unchecked",
  "-deprecation",
  "-encoding", "utf8",
  //"-language:existentials",
  //"-language:higherKinds",
  //"-language:implicitConversions",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import"
)

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")

libraryDependencies ++= Seq(
  //jdbc,
  //anorm,
  cache,
  ws,
  "org.mapdb" % "mapdb" % "1.0.7",
  "org.scalatest" %% "scalatest" % "2.2.2" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.1.2" % "test"
  //"net.sf.robocode" % "robocode.api" % "1.9.2.1",
)
