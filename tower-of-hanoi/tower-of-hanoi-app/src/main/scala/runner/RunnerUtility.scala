package runner

import domain._

object RunnerUtility {

  val usage = """
    Recall usage:
    tower-of-hanoi-app n start extra end
  """

  def parseArguments(args: Array[String]): CommandLineArgument = {

    val n = args(0).toInt
    val start = args(1).toString
    val extra = args(2).toString
    val end = args(3).toString

    CommandLineArgument(n, start, extra, end)

  }

  def persistOutput(commandLineArgument: CommandLineArgument, output: String, runTime: Double): Unit = {
    import commandLineArgument._

    println("====================================================================")
    println("INPUT & DIAGNOSTICS")

    println("  n       :  " + n.toString)
    println("  start   :  " + start)
    println("  extra   :  " + extra)
    println("  end     :  " + end)
    println("  runtime :  " + runTime.toString + "s")
    println("====================================================================")
    println()
    println(output)
  }

}