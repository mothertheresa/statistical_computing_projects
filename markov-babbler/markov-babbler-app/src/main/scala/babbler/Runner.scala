package babbler

import MarkovBabblerFunctions._
import RunnerUtilityFunctions._

object Runner extends App {

  override def main(args: Array[String]): Unit = {

    val startTime = System.nanoTime()

    val commandLineArguments = parseArguments(args)
    import commandLineArguments._

    val sequencedInputText = if (characters) {
      character_split(textInput, nchars.getOrElse(1))
    } else {
      sentence_split(textInput)
    }
    val markovChain = markov_train(sequencedInputText, order.getOrElse(1))
    val generatedText = markov_generate(markovChain, length, start = None)

    val endTime = System.nanoTime()
    val runTime = (endTime - startTime) / 10e9

    persistOutput(commandLineArguments, generatedText, runTime)

  }

}