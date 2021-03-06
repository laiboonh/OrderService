import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.11.11",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "OrderService",
    libraryDependencies ++= Seq(
      scalaTest % Test
      , akkaHttpTestkit % Test
      , akkaActor, akkaHttp, akkaStream, akkaSl4j, akkaHttpXml
      , logback % Runtime
    )
  )
