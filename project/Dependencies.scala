import sbt._

object Dependencies {
  val akkaVersion = "2.5.6"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"
  lazy val akkaActor =  "com.typesafe.akka" %% "akka-actor" % akkaVersion
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.10"
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  lazy val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10"
  lazy val akkaHttpXml = "com.typesafe.akka" %% "akka-http-xml" % "10.0.10"
  lazy val akkaSl4j = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.1.3"
}
