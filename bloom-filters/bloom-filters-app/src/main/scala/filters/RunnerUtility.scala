package filters

import domain._

import scala.io.Source

object RunnerUtilityFunctions {

  val usage = """
    Recall usage:
    bloom-filters-app numberOfBits numberOfHashes
  """

  def parseArguments(args: Array[String]): CommandLineArgument = {

    val numberOfBits = try {
      args(0).toInt
    } catch {
      case _ => throw new IllegalArgumentException("numberOfBits not defined as Int. " + usage)
    }

    val numberOfHashes = try {
      args(1).toInt
    } catch {
      case _ => throw new IllegalArgumentException("numberOfHashes not defined as Int. " + usage)
    }

    CommandLineArgument(numberOfBits, numberOfHashes)

  }

  def persistOutput(commandLineArgument: CommandLineArgument, output: String, runTime: Double): Unit = {
    import commandLineArgument._

    println("====================================================================")
    println("INPUT & DIAGNOSTICS")

    println("  numberOfBits    :  " + numberOfBits.toString)
    println("  numberOfHashes  :  " + numberOfHashes.toString)
    println("  runtime         :  " + runTime.toString + "s")
    println("====================================================================")
    println()
    println(output)
  }

}