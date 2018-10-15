package domain

import domain.TowerOfHanoiTypes._

/** Class that keeps track of the state of the game: the board and the number of moves
  *
  * @param state state of the game as represented by a map of pegs to disks
  * @param numberOfMoves the number of moves having been completed in the game
  */
case class Accumulator(state: Map[Peg, Seq[Disk]], numberOfMoves: Int) {

  override def toString: String = {
    val maxLength: Int = state.keys.map(_.length).max

    s"=========================" + "\n" +
      s"Number of moves: $numberOfMoves" + "\n" +
      s"-------------------------" + "\n" +
      state.map { case (peg, disks) =>
        val padding: Int = maxLength - peg.length
        s"${peg} " + (" " * padding) + ":    " + disks.reverse.map(d => d.toString).mkString(" ")
      }.mkString("\n") + "\n" +
      s"=========================" + "\n"
  }

}