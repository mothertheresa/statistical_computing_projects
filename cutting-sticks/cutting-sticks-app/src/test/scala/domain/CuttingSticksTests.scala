package domain

import CuttingSticks._
import org.scalatest._

class CuttingSticksTests extends FlatSpec with Matchers {

  "The best_cuts function" should "return proper output for case #1" in {
    val n = 10
    val cuts = Seq(2, 4, 7)

    val (minimumCost, bestCuts) = best_cuts(n, cuts)
    (minimumCost, bestCuts) shouldEqual (20, Seq(4, 2, 7))
  }

  "The best_cuts function" should "return proper output for case #2" in {
    val n = 10
    val cuts = Seq(4, 5, 7, 8)

    val (minimumCost, bestCuts) = best_cuts(n, cuts)
    (minimumCost, bestCuts) shouldEqual (22, Seq(4, 7, 5, 8))
  }

  "The best_cuts function" should "return proper output for case #3" in {
    val n = 100
    val cuts = Seq(25, 50, 75)

    val (minimumCost, bestCuts) = best_cuts(n, cuts)
    (minimumCost, bestCuts) shouldEqual (200, Seq(50, 25, 75))
  }

  "The best_cuts function when given no cuts" should "return no cuts" in {
    val n = 10
    val cuts = Seq()

    val (minimumCost, bestCuts) = best_cuts(n, cuts)
    (minimumCost, bestCuts) shouldEqual (0, Seq())
  }

  "The best_cuts function when given cuts larger than stick length" should "fail" in {
    val n = 10
    val cuts = Seq(5, 8, 12)

    an [IllegalArgumentException] should be thrownBy best_cuts(n, cuts)
  }

  "The best_cuts function when given repeated cuts" should "fail" in {
    val n = 10
    val cuts = Seq(5, 5)

    an [IllegalArgumentException] should be thrownBy best_cuts(n, cuts)
  }

}