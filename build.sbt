lazy val appVersion         = "0.0.1"
lazy val scalaformatVersion = "1.3.0"

lazy val scalacAppOptions = Seq(
  "-encoding",
  "UTF-8", // Specify character encoding used by source files.
  "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-unused",
  "-Ywarn-unused-import",
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-g:vars",
  "-Xlint:_"
)

lazy val sbtPackageClient = (project in file(".")).settings(
  sbtPlugin                 := true,
  name                      := "sbt-package-client",
  organization in ThisBuild := "org.max-peroch",
  version in ThisBuild      := appVersion,
  libraryDependencies += {
    val currentSbtVersion = (sbtBinaryVersion in pluginCrossBuild).value
    Defaults.sbtPluginExtra(
      "com.typesafe.sbt" % "sbt-native-packager" % "1.3.2" % "provided",
      currentSbtVersion,
      scalaBinaryVersion.value
    )
  },
  scalacOptions     := scalacAppOptions,
  scalafmtVersion   := scalaformatVersion,
  scalafmtOnCompile := true,
  // Bintray publish
  bintrayRepository   := "sbt-plugins",
  description         := "SBT plugin to package your client project before the packaging of the server.",
  publishMavenStyle   := false,
  bintrayOrganization := None,
  licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))
)
