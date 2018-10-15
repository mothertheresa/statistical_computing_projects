package babbler

import domain.{MarkovChain, Token}

import scala.util.Random

object MarkovBabblerFunctions {

  def sentence_split(inputText: String): Seq[Token] = {
    val result = inputText.split("\\s+").toSeq.map(Token(_)) // todo: don't remove any punctuation?
    //"[a-zA-Z]+".r.findAllIn(inputText).toSeq.map(t => Token(t.toLowerCase))

    if (result.isEmpty)
      throw new IllegalArgumentException("Input text contained no tokens under current splitting regime.")
    else {
      result
    }
  }

  def character_split(inputText: String, chars: Int): Seq[Token] = {
    val result = inputText.grouped(chars).toSeq.map(Token(_))

    if (result.isEmpty)
      throw new IllegalArgumentException("Input text contained no tokens under current splitting regime.")
    else {
      result
    }
  }

  def markov_train(sequence: Seq[Token], order: Int): MarkovChain = {
    if (sequence.length < order + 1)
      throw new IllegalArgumentException("Order specified is greater than length of text provided.")

    else {

      val chain = {
        sequence
          .sliding(order + 1) // get the token keys + token following (hence order + 1)
          .toList
          .groupBy { tokens =>
            val tokenKey = tokens.take(order) // group by token keys
            tokenKey
          }
          .map { case (tokenKey, values) => // massage the values so that they are frequency maps
            val nextTokens = values.map(_.last)
            val totalCount = nextTokens.length

            val nextTokensFrequencies = nextTokens
              .foldLeft[Map[Token, Double]](Map.empty) { case (map, token) =>
              map + (token -> {
                val tokenCount = map.getOrElse(token, 0.0) + (1.0 / totalCount)
                tokenCount
              })
            }

            (tokenKey, nextTokensFrequencies)
          }
      }

      MarkovChain(chain, order)

    }
  }

  def markov_generate(chain: MarkovChain, length: Int, start: Option[Seq[Token]]): Seq[Token] = {
    val startingTokens: Seq[Token] = start match {
      case Some(tokens) => if (chain.chain.contains(tokens)) {
        tokens
      } else {
        throw new IllegalArgumentException("Invalid start; remember it must exist in the chain.")
      }
      case None => chain.randomStart
    }

    var tokenList = startingTokens

    for( iteration <- 1 until (length-1) ){ // -1 for starting value
      val currentToken = tokenList.slice(from = iteration-1, until = iteration-1 + chain.order)
      val nextToken = chain.generate(currentToken) match {
        case Some(token) => token
        case None =>
          throw new IllegalArgumentException(s"No next token generated for token set $currentToken on iteration $iteration.")
      }
      tokenList = tokenList ++ Seq(nextToken)
    }

    tokenList
  }

}