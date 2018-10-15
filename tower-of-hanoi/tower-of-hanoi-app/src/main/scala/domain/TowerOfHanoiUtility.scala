package domain

import domain.TowerOfHanoiTypes.{Disk, Peg}

object TowerOfHanoiUtility {

  def isEven(n: Int): Boolean = {
    (n % 2) == 0
  }

  /** Generates initial state of accumulator
    *
    *  @param n number of disks
    *  @param start label of starting peg
    *  @param extra label of extra peg
    *  @param end label of ending peg
    *  @return initial state of accumulator to begin the game,
    *          where all pegs are on the starting peg.
    */
  def initialState(n: Int,
                   start: Peg,
                   extra: Peg,
                   end: Peg): Accumulator = {

    Accumulator(
      state = Map(start -> Seq.range(0, n), // top to bottom
                  extra -> Seq.empty,
                  end -> Seq.empty),
      numberOfMoves = 0)

  }

  def runChecks(disk: Disk, from: Peg, to: Peg, acc: Accumulator): Unit = {
    if (from == to) {
      throw new IllegalArgumentException("Ensure 'from' and 'to' pegs are different.")
    }

    if (!acc.state.contains(from)) {
      throw new IllegalArgumentException("Ensure 'from' peg is contained in accumulator.")
    }

    if (!acc.state.contains(to)) {
      throw new IllegalArgumentException("Ensure 'to' peg is contained in accumulator.")
    }

    if (acc.state(from).isEmpty) {
      throw new IllegalArgumentException("Ensure 'from' peg contains disks.")
    }

    if (acc.state(from).head != disk) {
      throw new IllegalArgumentException(s"Ensure first element (${acc.state(from).head}) on 'from' " +
        s"peg (${from}) is desired disk (${disk}) with 'to' peg (${to}).")
    }

    acc.state(to).lift(0) match {
      case Some(toDisk) =>
        if (toDisk < disk) {
          throw new IllegalArgumentException(s"Ensure next element (${toDisk}) on 'to' " +
            s"peg (${to}) is larger than (${disk}) on 'from' peg (${from}).")
        }
      case None => ()
    }
  }

}