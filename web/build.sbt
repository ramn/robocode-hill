import NativePackagerKeys._

name := """robocode-hill"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

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
  "-Xfuture"
  //,"-Ywarn-unused-import"
)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")

resolvers +=
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  //jdbc,
  //anorm,
  cache,
  ws,
  "joda-time" % "joda-time" % "2.7",
  "commons-io" % "commons-io" % "2.4",
  //"org.mapdb" % "mapdb" % "1.0.7",
  //"org.mapdb" % "mapdb" % "2.0-alpha1",
  "org.mapdb" % "mapdb" % "2.0.0-SNAPSHOT",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.1.2" % "test"
  //"net.sf.robocode" % "robocode.api" % "1.9.2.1",
)
