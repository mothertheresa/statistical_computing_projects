package domain

object CuttingSticks {

  /** Computes minimum cut cost and optimal cutting coordinates
    *
    * @param stick_length length of the stick
    * @param cuts sequence of cutting locations
    * @return minimum cost of cuts and the sequence in which cuts should be performed
    */
  def best_cuts(stick_length: Int, cuts: Seq[Int]): (Int, Seq[Int]) = {

    if (cuts.isEmpty) {

      (noCutCost, noCutSeq)

    } else {

      run_checks(stick_length, cuts)

      val allCuts: Seq[Int] = Seq(0) ++ cuts ++ Seq(stick_length)
      val numCuts: Int = allCuts.length

      // stepwise populate DP table
      val (cost, cutSequence) = generate_tables(allCuts, numCuts)

      // lookup values in DP table
      (cost((0, numCuts - 1)), cutSequence((0, numCuts - 1)))
    }

  }

  /** Generates and step-wise populates optimal cost and sequence tables with dynamic programming
    *
    * @param allCuts length of the stick
    * @param numCuts sequence of cutting locations
    * @return the optimal cost table (leftCut, rightCut -> cost), and the optimal sequence table
    *         (leftCut, rightCut -> sequence of cuts)
    */
  def generate_tables(allCuts: Seq[Int], numCuts: Int):(Map[(Int, Int), Int], Map[(Int, Int), Seq[Int]]) = {

    var cost: Map[(Int, Int), Int] = Map.empty
    var cutSequence: Map[(Int, Int), Seq[Int]] = Map.empty

    Seq.range(1, numCuts).foreach { rightCut =>

      Seq.range(0, rightCut - 1).reverse.foreach { leftCut =>
        cost = cost.updated((leftCut, rightCut), upperBound)

        // find best cut
        Seq.range(leftCut+1, rightCut).foreach { middleCut =>
          val newCost = cost.getOrElse((leftCut, middleCut), noCutCost) +
            cost.getOrElse((middleCut, rightCut), noCutCost)

          // if new cost is better, then update optimal cost and sequence data
          if (newCost < cost((leftCut, rightCut))) {
            cost = cost.updated((leftCut, rightCut), newCost)

            val newCutSequence: Seq[Int] = Seq(allCuts(middleCut)) ++
              cutSequence.getOrElse((leftCut, middleCut), noCutSeq) ++
              cutSequence.getOrElse((middleCut, rightCut), noCutSeq)
            cutSequence = cutSequence.updated((leftCut, rightCut), newCutSequence)
          }
        }

        // include length in cost
        val length = allCuts(rightCut) - allCuts(leftCut)
        val totalCost = cost((leftCut, rightCut)) + length
        cost = cost.updated((leftCut, rightCut), totalCost)
      }

    }

    (cost, cutSequence)
  }

  def run_checks(stick_length: Int, cuts: Seq[Int]): Unit = {

    if (cuts.max >= stick_length) {
      throw new IllegalArgumentException(s"Stick length ${stick_length} must exceed maximum cut ${cuts.max}.")
    }

    if (cuts.distinct.length != cuts.length) {
      throw new IllegalArgumentException(s"Cuts ${cuts} must be unique.")
    }

  }

  val upperBound: Int = 10e6.toInt

  val noCutCost: Int = 0
  val noCutSeq: Seq[Int] = Seq.empty

}