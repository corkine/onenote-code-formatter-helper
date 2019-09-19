lazy val commonSettings = Seq(
  organization := "com.mazhangjing",
  version := "0.1.0",
  scalaVersion := "2.12.8",
  libraryDependencies := deps
)

lazy val deps = Seq(
  "org.scalafx" %% "scalafx" % "8.0.144-R12"
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "cleanApp",
    mainClass in (Compile, packageBin) := Some("com.mazhangjing.HelloAgain")
  )