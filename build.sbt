name := "practical-functional-programming"

version := "0.1"

scalaVersion := "2.13.6"

idePackagePrefix := Some("berlin.code.functional")

scalafmtOnCompile := true

scalacOptions ++= Seq(
  "-Ywarn-unused",
  "-Xfatal-warnings",
  "-Xlint:_,-byname-implicit",
)