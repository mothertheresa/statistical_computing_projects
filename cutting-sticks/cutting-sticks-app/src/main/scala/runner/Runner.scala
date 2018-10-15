package runner

import RunnerUtility._
import domain.CuttingSticks._

object Runner extends App {

  override def main(args: Array[String]): Unit = {

    val startTime = System.nanoTime()

    val commandLineArguments = parseArguments(args)
    import commandLineArguments._

    val (cost, cutOrder) = best_cuts(stickLength, cuts)
    val output = resultsToString(cost, cutOrder)

    val endTime = System.nanoTime()
    val runTime = (endTime - startTime) / 10e9

    persistOutput(commandLineArguments, output, runTime)

  }

}