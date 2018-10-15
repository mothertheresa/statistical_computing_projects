package runner

import domain._

object RunnerUtility {

  val usage = """
    Recall usage:
    cutting-sticks-app stickLength cuts
  """

  def parseArguments(args: Array[String]): CommandLineArgument = {

    val stickLength = args(0).toInt
    val cuts = args.drop(1).map(_.toInt)

    CommandLineArgument(stickLength, cuts)

  }

  def resultsToString(optimalCost: Int, optimalCuts: Seq[Int]): String = {
    "Optimal Cost: " + optimalCost + "\n" +
    "Optimal Cuts: " + optimalCuts.mkString(", ")
  }

  def persistOutput(commandLineArgument: CommandLineArgument, output: String, runTime: Double): Unit = {
    import commandLineArgument._

    println("====================================================================")
    println("INPUT & DIAGNOSTICS")

    println("  stickLength :  " + stickLength.toString)
    println("  cuts        :  " + cuts.mkString(", "))
    println("  runtime     :  " + runTime.toString + "s")
    println("====================================================================")
    println()
    println(output)
  }

}