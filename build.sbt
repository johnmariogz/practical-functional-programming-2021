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
  "org.http4s" %% "http4s-blaze-server" % "0.23.6",
  "org.http4s" %% "http4s-dsl" % "0.23.6",
  "org.http4s" %% "http4s-server" % "0.23.6",
  "org.http4s" %% "http4s-circe" % "0.23.6",
  "io.circe"   %% "circe-generic" % "0.14.1",
  "org.typelevel" %% "cats-effect" % "3.2.9",
  "com.typesafe.akka" %% "akka-actor" % "2.6.17",
  "com.typesafe.akka" %% "akka-stream" % "2.6.17",
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4",
  "org.tpolecat"  %% "doobie-core"     % "1.0.0-RC1",
  "org.tpolecat"  %% "doobie-hikari"   % "1.0.0-RC1",
  "org.tpolecat"  %% "doobie-postgres" % "1.0.0-RC1",
  "io.zonky.test" % "embedded-postgres" % "1.3.1",
  "org.flywaydb" % "flyway-core" % "8.0.5",
  "com.typesafe.akka" %% "akka-testkit" % "2.6.17" % Test,
  "org.scalatest" %% "scalatest" % "3.2.10" % Test
)

Test / fork := true