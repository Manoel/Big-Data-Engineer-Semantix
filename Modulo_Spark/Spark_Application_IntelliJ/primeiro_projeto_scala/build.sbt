ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.11.12"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.1" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.1" % "provided"

lazy val root = (project in file("."))
  .settings(
    name := "primeiro_projeto_scala"
  )
