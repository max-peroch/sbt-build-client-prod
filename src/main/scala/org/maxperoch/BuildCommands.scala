package org.maxperoch

import java.io.File

import sbt.Logger

import scala.language.implicitConversions
import scala.sys.process.{ Process, ProcessBuilder, ProcessLogger }

class BuildCommands(log: Logger) {

  import Helpers.echo

  private lazy implicit val defaultLabel: Label = Label("client", Magenta)
  private lazy val successMessage               = SuccessMessage("Client frontend successfully built for prod !")
  private lazy val failureMessage               = FailureMessage("Something went wrong while building the Client frontend")
  private lazy val noDirWarning                 = "Client directory doesn't exist"
  private lazy val noCmdsWarning                = "There are no commands to run"

  private def logInfo(message: String): Unit       = log.info(message)
  private def logWarn(message: String): Unit       = log.warn(message)
  implicit def messageToString(m: Message): String = m.toString

  private def toCommands(cmds: Seq[String], dir: File): Seq[ProcessBuilder] =
    cmds.map(c => Seq(echo(c), Process(c, dir))).flatten

  private def chainCommands(cmds: Seq[ProcessBuilder]): Int =
    cmds.reduce(_ #&& _) !
      ProcessLogger(
        (s: String) => logInfo(OkMessage(s)),
        (e: String) => logInfo(KoMessage(e))
      )

  private def printResult(result: Int): Message =
    if (result == 0) successMessage else failureMessage

  def build(commands: Seq[String], directory: File): Unit = (commands, directory) match {
    case (_, dir) if !dir.exists => logWarn(noDirWarning)
    case (Nil, _)                => logWarn(noCmdsWarning)
    case (cmds, dir)             => logInfo(printResult(chainCommands(toCommands(cmds, dir))))
  }
}
