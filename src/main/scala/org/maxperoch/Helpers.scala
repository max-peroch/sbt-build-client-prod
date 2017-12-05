package org.maxperoch

sealed abstract class Color(color: String) {
  protected val reset  = "\u001b[0m"
  def apply(s: String) = s"$color$s$reset"
}

sealed abstract class Color_B(color: String) extends Color(color) {
  override protected val reset = "\u001b[49m"
}

case object Magenta      extends Color("\u001b[35m")
case object Red          extends Color("\u001b[31m")
case object Green        extends Color("\u001b[32m")
case object White        extends Color("\u001b[37m")
case object Black        extends Color("\u001b[30m")
case object Magenta_B    extends Color_B("\u001b[45m")
case object LightRed_B   extends Color_B("\u001b[101m")
case object LightGreen_B extends Color_B("\u001b[102m")

case class Label(value: String, color: Color) { override def toString = s"[${color.apply(value)}]" }

sealed abstract class Message(value: String, color: Color)(implicit label: Label) {
  override def toString = s"$label ${color.apply(value)}"
}

case class OkMessage(value: String)(implicit label: Label)      extends Message(value, Green)
case class KoMessage(value: String)(implicit label: Label)      extends Message(value, Red)
case class SuccessMessage(value: String)(implicit label: Label) extends Message(Black(Helpers.pad(value)), LightGreen_B)
case class FailureMessage(value: String)(implicit label: Label) extends Message(Black(Helpers.pad(value)), LightRed_B)

object Helpers {
  import scala.sys.process.Process

  def pad(value: String) = s" $value "

  def echo(cmd: String) = Process(s"echo ${Magenta_B(White(pad(s"> $cmd")))}")
}
