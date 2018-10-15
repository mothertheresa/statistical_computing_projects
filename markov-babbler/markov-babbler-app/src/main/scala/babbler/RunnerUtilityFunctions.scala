package babbler

import domain._

import scala.io.Source

object RunnerUtilityFunctions {

  val usage = """
    Recall usage:
    markov-babbler-app --length num [--order num] [--characters] [--nchars num] --file text OR --stdin text
  """

  def parseArguments(args: Array[String]): CommandLineArgument = {

    val arglist = args.toList
    type OptionMap = Map[Symbol, List[String]]

    def nextOption(map : OptionMap, list: List[String]) : OptionMap = {
      list match {
        case Nil => map
        case "--order" :: value :: tail =>
          nextOption(map ++ Map('order -> List(value)), tail)
        case "--length" :: value :: tail =>
          nextOption(map ++ Map('length -> List(value)), tail)
        case "--characters" :: tail =>
          nextOption(map ++ Map('characters -> List("")), tail)
        case "--nchars" :: value :: tail =>
          nextOption(map ++ Map('nchars -> List(value)), tail)
        case "--file" :: value :: tail =>
          nextOption(map ++ Map('file -> List(value)), tail)
        case "--link" :: value :: tail =>
          nextOption(map ++ Map('link -> List(value)), tail)
        case "--stdin" :: tail =>
          map ++ Map('stdin -> tail)
        case option :: tail => println("Unknown option " + option)
          sys.exit(1)
      }
    }
    val options = nextOption(Map(), arglist)

    val order = options.get('order).map(_.head.toInt)
    val length = options.get('length).map(_.head.toInt).getOrElse(
      throw new IllegalArgumentException("Missing length; required argument. " + usage)
    )
    val characters: Boolean = options.contains('characters)
    val nchars = options.get('nchars).map(_.head.toInt)
    val textInput: String = if (options.contains('file)) {
      text_ingest(options('file).head)
    } else if (options.contains('stdin)) {
      stdin_ingest(options('stdin))
    } else if (options.contains('link)) {
      rss_ingest(options('link).head)
    } else {
      throw new IllegalArgumentException("Invalid or missing text input. " + usage)
    }

    if (nchars.isDefined && !characters) {
      throw new IllegalArgumentException("'nchars' defined but 'character' flag missing. " + usage)
    }

    if (length < order.getOrElse(1)) {
      throw new IllegalArgumentException("Length cannot be less than order.")
    }

    CommandLineArgument(length, order, characters, nchars, textInput)

  }

  def text_ingest(filename: String): String = Source.fromFile(filename).mkString

  def stdin_ingest(standardInput: Seq[String]): String = standardInput.mkString(" ")

  def rss_ingest(link: String): String = XmlParser.parse(link)

  def persistOutput(commandLineArgument: CommandLineArgument, generatedText: Seq[Token], runTime: Double): Unit = {
    // theoretically this could also output to a file, for example
    import commandLineArgument._
    val prettyPrintedText: String = generatedText.map(_.value).mkString(" ")

    println("====================================================================")
    println("INPUT & DIAGNOSTICS")

    println("  textInput :  " + textInput.take(15) + "...")
    println("  length    :  " + length.toString)
    println("  order     :  " + order.map(_.toString).getOrElse("default (1)"))
    println("  characters:  " + characters.toString)
    println("  nchars    :  " + nchars.map(_.toString).getOrElse("default (1)"))
    println("  runtime   :  " + runTime.toString + "s")
    println("====================================================================")
    println()
    println(prettyPrintedText)
  }

}