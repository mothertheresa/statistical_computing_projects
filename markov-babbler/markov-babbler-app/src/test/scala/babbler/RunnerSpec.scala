package babbler

import domain.{MarkovChain, Token}
import MarkovBabblerFunctions._
import RunnerUtilityFunctions._
import org.scalatest._

class RunnerSpec extends FlatSpec with Matchers {

  val smallerSampleText = "The brown dog jumped over the dog."
  val sampleText = "This is some sample text! It's for testing purposes - that is all. Is it good?"

  "The parseArguments function" should "throw an error when it's missing arguments" in {
    assertThrows[IllegalArgumentException] {
      parseArguments(Array("--file", "filenameButNoLength"))
    }
  }

  "The parseArguments function" should "throw an error when nchars defined but not character" in {
    assertThrows[IllegalArgumentException] {
      parseArguments(Array("--length", "10", "--nchars", "2", "--file", "filenameButNoLength"))
    }
  }

  "The parseArguments function" should "throw an error when length is less than order" in {
    assertThrows[IllegalArgumentException] {
      parseArguments(Array("--length", "3", "--order", "5", "--file", "filename"))
    }
  }

  "The stdin_ingest function" should "create a concatenated string" in {
    val stdInput: Seq[String] = smallerSampleText.split("\\s+")
    val expectedResult: String = smallerSampleText
    stdin_ingest(stdInput) shouldEqual expectedResult
  }

  "The sentence_split function" should "split on spaces" in {
    val expectedResult: Seq[Token] = Seq(
      Token("The"),
      Token("brown"),
      Token("dog"),
      Token("jumped"),
      Token("over"),
      Token("the"),
      Token("dog."))
    sentence_split(smallerSampleText) shouldEqual expectedResult
  }

  "The character_split function" should "split on characters" in {
    val expectedResult: Seq[Token] = Seq(
      Token("Hel"),
      Token("lo "),
      Token("wor"),
      Token("ld."))
    character_split("Hello world.", chars = 3) shouldEqual expectedResult
  }

  "The markov_train function" should "properly count repeated words" in {
    val order: Int = 1
    val sequence: Seq[Token] = Seq(
      Token("the"),
      Token("brown"),
      Token("dog"),
      Token("jumped"),
      Token("over"),
      Token("the"),
      Token("black"),
      Token("dog."))

    val chain: Map[Seq[Token], Map[Token, Double]] = Map(
      Seq(Token("the")) -> Map(
        Token("brown") -> 0.5,
        Token("black") -> 0.5
      ),
      Seq(Token("brown")) -> Map(
        Token("dog") -> 1.0
      ),
      Seq(Token("dog")) -> Map(
        Token("jumped") -> 1.0
      ),
      Seq(Token("jumped")) -> Map(
        Token("over") -> 1.0
      ),
      Seq(Token("over")) -> Map(
        Token("the") -> 1.0
      ),
      Seq(Token("black")) -> Map(
        Token("dog.") -> 1.0
      )
    )
    val expectedMarkovChain = MarkovChain(chain, order)
    markov_train(sequence, order) shouldEqual expectedMarkovChain
  }

  "The markov_train function" should "properly construct a 2-order chain" in {
    val order: Int = 2
    val sequence: Seq[Token] = Seq(
      Token("the"),
      Token("brown"),
      Token("the"),
      Token("brown"),
      Token("tree"))

    val chain: Map[Seq[Token], Map[Token, Double]] = Map(
      Seq(Token("the"), Token("brown")) -> Map(
        Token("the") -> 0.5,
        Token("tree") -> 0.5
      ),
      Seq(Token("brown"), Token("the")) -> Map(
        Token("brown") -> 1.0
      )
    )
    val expectedMarkovChain = MarkovChain(chain, order)
    markov_train(sequence, order) shouldEqual expectedMarkovChain
  }

  "The markov_generate function" should "construct an output of the right length" in {
    val length: Int = 2
    val chain: Map[Seq[Token], Map[Token, Double]] = Map(
      Seq(Token("the"), Token("brown")) -> Map(
        Token("the") -> 0.5,
        Token("tree") -> 0.5
      ),
      Seq(Token("brown"), Token("the")) -> Map(
        Token("brown") -> 1.0
      )
    )
    val markovChain = MarkovChain(chain, order = 2)
    markov_generate(markovChain, length, None).length shouldEqual length
  }

  "The markov_generate function" should "construct an output with the right start" in {
    val startingToken = Token("the")
    val start: Option[Seq[Token]] = Some(Seq(startingToken))
    val length: Int = 2
    val chain: Map[Seq[Token], Map[Token, Double]] = Map(
      Seq(Token("the")) -> Map(
        Token("the") -> 0.5,
        Token("tree") -> 0.5
      ),
      Seq(Token("brown")) -> Map(
        Token("brown") -> 1.0
      )
    )
    val markovChain = MarkovChain(chain, order = 1)
    markov_generate(markovChain, length, start).head shouldEqual startingToken
  }

  "The markov_generate function" should "throw an error if the start isn't in the chain" in {
    val startingToken = Token("unknown")
    val start: Option[Seq[Token]] = Some(Seq(startingToken))
    val length: Int = 2
    val chain: Map[Seq[Token], Map[Token, Double]] = Map(
      Seq(Token("the")) -> Map(
        Token("tree") -> 1.0
      )
    )
    val markovChain = MarkovChain(chain, order = 1)
    assertThrows[IllegalArgumentException] {
      markov_generate(markovChain, length, start)
    }
  }

}