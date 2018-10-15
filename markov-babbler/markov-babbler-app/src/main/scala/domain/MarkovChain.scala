package domain

import scala.util.Random
import scala.util.Random._

trait IMarkovChain {
  val order: Int
  def randomStart: Seq[Token]
  def generate(tokens: Seq[Token]): Option[Token]
}

case class MarkovChain(chain: Map[Seq[Token], Map[Token, Double]], order: Int) extends IMarkovChain {

  def randomStart: Seq[Token] = chain.keys.toIndexedSeq(Random.nextInt(chain.keys.size))

  def generate(tokens: Seq[Token]): Option[Token] = {
    val a = nextDouble()

    val nextTokensPMF = chain.getOrElse(tokens, Map.empty)
    val nextTokensCDF = generateTokenCDF(nextTokensPMF)

    nextTokensCDF
      .find{ case (_, cdfLower, cdfUpper) => cdfLower < a && a < cdfUpper }
      .map{ case (token, _, _) => token }
  }

  private def generateTokenCDF(tokenPMF: Map[Token, Double]): Seq[(Token, Double, Double)] = {
    tokenPMF
      .foldLeft[Seq[(Token, Double, Double)]](Seq.empty){ case (runningTotalSeq, (token, probability)) =>

      val cumulativeCDFs = runningTotalSeq.map{ case (token, lowerCdf, upperCdf) => upperCdf }
      val cumulativeCDF = if (cumulativeCDFs.isEmpty) 0.0 else cumulativeCDFs.max

      runningTotalSeq ++ Seq((token, cumulativeCDF, probability + cumulativeCDF))
    }
  }

}