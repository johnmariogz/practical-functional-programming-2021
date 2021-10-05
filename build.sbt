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
  "org.scalatest" %% "scalatest" % "3.2.10" % Test
)

Test / fork := true