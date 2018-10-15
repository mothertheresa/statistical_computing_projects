package domain

case class CommandLineArgument(length: Int,
                               order: Option[Int],
                               characters: Boolean,
                               nchars: Option[Int],
                               textInput: String)