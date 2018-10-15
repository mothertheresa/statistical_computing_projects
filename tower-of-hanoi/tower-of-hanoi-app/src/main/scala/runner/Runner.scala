package runner

import RunnerUtility._
import domain.TowerOfHanoiUtility.initialState
import domain.TowerOfHanoiRecursive._

object Runner extends App {

  override def main(args: Array[String]): Unit = {

    val startTime = System.nanoTime()

    val commandLineArguments = parseArguments(args)
    import commandLineArguments._

    val acc = initialState(n, start, extra, end)
    val output = hanoi(n, start, extra, end, movefn, acc).toString()

    val endTime = System.nanoTime()
    val runTime = (endTime - startTime) / 10e9

    persistOutput(commandLineArguments, output, runTime)

  }

}