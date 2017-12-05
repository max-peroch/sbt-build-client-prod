package org.maxperoch

import com.typesafe.sbt.packager.universal.UniversalPlugin
import com.typesafe.sbt.packager.universal.UniversalPlugin.autoImport.{ dist, stage, Universal }
import sbt.Keys.{ baseDirectory, streams }
import sbt.plugins.JvmPlugin
import sbt.{ AutoPlugin, Setting, settingKey, taskKey, _ }

object PackageClientPlugin extends AutoPlugin {

  private val defaultClientDirectoryName = "client"

  object autoImport {

    val buildClient =
      taskKey[Unit]("Task which use the `buildCommands` to package the client located in `clientDirectory`")
    val buildCommands   = settingKey[Seq[String]]("Sequence of bash instructions to package the client ")
    val clientDirectory = settingKey[File]("Location of the client directory on which to apply the `buildCommands`")

    lazy val baseUniversalSettings: Seq[Def.Setting[_]] = Seq(
      buildClient := {

        val streamsValue = streams.value
        val streamsLog   = streamsValue.log
        new BuildCommands(streamsLog).build(buildCommands.value, clientDirectory.value)
      },
      buildCommands   := (buildCommands in ThisScope).value,
      clientDirectory := (clientDirectory in ThisScope).value,
      stage           := (stage dependsOn buildClient).value,
      dist            := (dist dependsOn buildClient).value
    )

    lazy val defaultUniversalSettings: Seq[Def.Setting[_]] = Seq(
      buildCommands   := Seq(),
      clientDirectory := baseDirectory.value / defaultClientDirectoryName,
    )
  }

  import autoImport._

  override def requires = JvmPlugin && UniversalPlugin

  override def trigger = allRequirements

  override val projectSettings: Seq[Setting[_]] = defaultUniversalSettings ++ inConfig(Universal)(baseUniversalSettings)

}
