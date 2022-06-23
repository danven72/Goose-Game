ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val scalaTestVersion = "3.2.12"
val mockitoVersion   = "3.2.12.0"

lazy val root = (project in file("."))
  .settings(
    name := "Goose Game"
  )

libraryDependencies ++= Seq(
  "org.scalatest"     %% "scalatest"   % scalaTestVersion,
  "org.scalatest"     %% "scalatest"   % scalaTestVersion % "test",
  "org.scalatestplus" %% "mockito-4-5" % mockitoVersion   % "test"
)
