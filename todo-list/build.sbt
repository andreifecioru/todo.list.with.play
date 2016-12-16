name := """todo-list"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  ws,
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",

  // allows us to use bind[] APIs in the Module definitions
  "net.codingwell" %% "scala-guice" % "4.0.1",

  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

