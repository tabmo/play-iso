import sbt._
import Keys._
import Dependencies._

lazy val buildSettings = Seq(
  organization := "io.tabmo",
  scalaVersion := Version.scala
)

lazy val compilerOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-unchecked",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture"
)

lazy val macroProjectSettings = Seq(
  libraryDependencies += "org.scala-lang" % "scala-reflect" % Version.scala
)

lazy val baseSettings = Seq(
  scalacOptions in (Compile, console) := compilerOptions,
  scalacOptions in (Compile, test) := compilerOptions,
  resolvers ++= Seq(
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
    Resolver.sonatypeRepo("releases")
  )
)

lazy val publishSettings = Seq(
  licenses += ("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0")),
  bintrayOrganization := Some("tabmo")
)

lazy val allSettings = buildSettings ++ baseSettings ++ publishSettings

lazy val isoMacro = (project in file("macro")).settings(
  name := "play-iso-macro-slick",
  libraryDependencies ++= Seq(
    "com.typesafe.slick" %% "slick" % Version.Dependencies.slick
  )
) .settings(allSettings: _*)
  .settings(macroProjectSettings: _*)

lazy val root = (project in file(".")).settings(
  name := "play-iso-slick",
  mappings in (Compile, packageBin) ++= mappings.in(isoMacro, Compile, packageBin).value,
  mappings in (Compile, packageSrc) ++= mappings.in(isoMacro, Compile, packageSrc).value,
  libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play" % Version.Dependencies.play,
    "org.slf4j" % "slf4j-nop" % "1.7.18" % "provided"
  )
) .settings(allSettings: _*)
  .dependsOn(isoMacro).aggregate(isoMacro)
