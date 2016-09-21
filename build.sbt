
name := "jet-stream"

version := scala.util.Properties.envOrElse("APP_VERSION", "snapshot")

scalaVersion := "2.11.8"

sbtVersion := "0.13.11"

scalacOptions ++= Seq("-Xfatal-warnings", "-feature")

val unfilteredLibraryVersion = "0.8.4"

val circeVersion = "0.5.1"

libraryDependencies ++= Seq(
  "io.circe"                               %% "circe-core"               % circeVersion,
  "io.circe"                               %% "circe-generic"            % circeVersion,
  "io.circe"                               %% "circe-parser"             % circeVersion,
  "io.circe"                               %% "circe-optics"             % circeVersion,
  "org.scalaz.stream"                      %% "scalaz-stream"            % "0.8.3",
  "net.databinder.dispatch"                %% "dispatch-core"            % "0.11.2",
  "commons-lang"                           %  "commons-lang"             % "2.6",
  "org.slf4j"                              %  "jul-to-slf4j"             % "1.7.7",
  "ch.qos.logback"                         %  "logback-classic"          % "1.1.2",
  "org.specs2"                             %% "specs2"                   % "2.4.15" % "test"
)


scalafmtConfig in ThisBuild := Some(file(".scalafmt"))

compileInputs in (Compile, compile) <<=
  (compileInputs in (Compile, compile)) dependsOn (scalafmt in Compile)


mainClass in Compile := Some("app.Main") //Used in Universal packageBin

mainClass in (Compile, run) := Some("app.Main") //Used from normal sbt

enablePlugins(JavaServerAppPackaging, DockerPlugin)

dockerRepository := Some("cwmyers")

dockerBaseImage := scala.util.Properties.envOrElse("DOCKER_IMAGE", "openjdk:latest")

packageName in Docker := scala.util.Properties.envOrElse("DOCKER_PACKAGE_NAME", packageName.value)
