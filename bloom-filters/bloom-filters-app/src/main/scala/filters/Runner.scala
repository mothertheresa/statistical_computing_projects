package filters

import RunnerUtilityFunctions._
import domain.StringBloomFilter

object Runner extends App {

  override def main(args: Array[String]): Unit = {

    val startTime = System.nanoTime()

    val commandLineArguments = parseArguments(args)
    import commandLineArguments._

    //val output = new StringBloomFilter(numberOfBits, numberOfHashes)
    // future extension could be to make bloom filter for all words in a text file

    val endTime = System.nanoTime()
    val runTime = (endTime - startTime) / 10e9

    persistOutput(commandLineArguments, "...", runTime)

  }

}