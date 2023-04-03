name := "Code Formatter Helper"
organization := "com.mazhangjing"
version := "0.1.0"
scalaVersion := "2.12.8"
libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "8.0.144-R12",
  "org.jsoup" % "jsoup" % "1.12.1"
)

resolvers += Opts.resolver.sonatypeSnapshots

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xlint")
fork in run := true
mainClass in (Compile, packageBin) := Some("com.mazhangjing.PasteApp")

lazy val iconGlob = sys.props("os.name").toLowerCase match {
  case os if os.contains("mac") => "*.icns"
  case os if os.contains("win") => "*.ico"
  case _ => "*.png"
}

enablePlugins(JDKPackagerPlugin)
jdkPackagerJVMArgs := Seq("-Xmx1g")
maintainer := "CorkineMa"
packageSummary := "代码格式化小工具"
packageDescription := "一个帮助快速格式化代码复制的小工具，用于剪贴代码片段到 OneNote 上。"
jdkPackagerProperties := Map("app.name" -> name.value, "app.version" -> version.value)
jdkPackagerAppArgs := Seq(maintainer.value, packageSummary.value, packageDescription.value)
jdkPackagerType := "image"
jdkAppIcon := (sourceDirectory.value ** iconGlob).getPaths.headOption.map(file)