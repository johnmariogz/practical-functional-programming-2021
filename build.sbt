name := "practical-functional-programming"

version := "0.1"

scalaVersion := "2.13.6"

scalafmtOnCompile := true

scalacOptions ++= Seq(
  "-Ywarn-unused",
  "-Xfatal-warnings",
  "-Xlint:_,-byname-implicit",
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.6.1",
  "com.typesafe.akka" %% "akka-actor" % "2.6.17",
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4",
  "com.typesafe.akka" %% "akka-testkit" % "2.6.17" % Test,
  "org.scalatest" %% "scalatest" % "3.2.10" % Test
)

Test / fork := true