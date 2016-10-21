
name := "jet-stream"

version := scala.util.Properties.envOrElse("APP_VERSION", "snapshot")

scalaVersion := "2.11.8"

sbtVersion := "0.13.11"

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-Ywarn-unused-import",
  "-feature")

val circeVersion = "0.5.1"
val fs2Version   = "0.9.1"

libraryDependencies ++= Seq(
  "io.circe"                               %% "circe-core"               % circeVersion,
  "io.circe"                               %% "circe-generic"            % circeVersion,
  "io.circe"                               %% "circe-parser"             % circeVersion,
  "io.circe"                               %% "circe-optics"             % circeVersion,
  "co.fs2"                                 %% "fs2-core"                 % fs2Version,
  "co.fs2"                                 %% "fs2-io"                   % fs2Version,
  "commons-io"                             % "commons-io"                % "2.4",
  "net.databinder.dispatch"                %% "dispatch-core"            % "0.11.2",
  "commons-lang"                           %  "commons-lang"             % "2.6",
  "org.slf4j"                              %  "jul-to-slf4j"             % "1.7.7",
  "ch.qos.logback"                         %  "logback-classic"          % "1.1.2",
  "org.specs2"                             %% "specs2-core"              % "3.8.4"      % "test"
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
