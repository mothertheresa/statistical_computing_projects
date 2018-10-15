package domain

import TowerOfHanoiUtility._
import TowerOfHanoiTypes._

object TowerOfHanoiIterative {

  /** Runs the tower of hanoi game given disk number, peg labels, and an accumulator
    *
    * @param n number of disks
    * @param start label of starting peg (where all disks start)
    * @param extra label of extra peg
    * @param end label of end peg (where all disks should end up)
    * @param movefn function that updates the accumulator when a move occurs
    * @param acc the accumulator that keeps track of the state of the game
    * @return the accumulator (state) at the end of the game
    */
  def hanoi(n: Int,
            start: Peg,
            extra: Peg,
            end: Peg,
            movefn: (Int, Peg, Peg, Accumulator) => Accumulator,
            acc: Accumulator): Accumulator = {

    var accum = acc

    if (isEven(n)) {

      while (!gameIsComplete(accum, end, n)) {
        accum = nextMove(start, extra, accum).getOrElse(return accum)
        accum = nextMove(start, end,   accum).getOrElse(return accum)
        accum = nextMove(extra, end,   accum).getOrElse(return accum)
      }
      return accum

    } else {

      while (!gameIsComplete(accum, end, n)) {
        accum = nextMove(start, end,   accum).getOrElse(return accum)
        accum = nextMove(start, extra, accum).getOrElse(return accum)
        accum = nextMove(extra, end,   accum).getOrElse(return accum)
      }
      return accum

    }

  }

  /** Updated the state of the accumulator by moving a disk from one peg to another
    *
    *  @param disk disk to be moved, must be first disk on the 'from' peg
    *  @param from peg which the disk should be moved from
    *  @param to peg to which the disk should be moved, must be different from 'from'
    *  @param acc the current accumulator (state) of the 'board'
    *  @return new updated accumulator with peg appropriately moved.
    */
  def movefn(disk: Disk, from: Peg, to: Peg, acc: Accumulator): Accumulator = {
    runChecks(disk: Disk, from: Peg, to: Peg, acc: Accumulator)

    val newFrom = from -> acc.state(from).drop(1)
    val newTo = to -> (disk +: acc.state(to))

    val state = (acc.state + newFrom) + newTo
    val numberOfMoves = acc.numberOfMoves + 1

    Accumulator(state, numberOfMoves)
  }

  /** If possible, finds a legal move between two pegs given a particular state of the game
    *
    *  @param peg1 peg from which disk could be taken or moved to
    *  @param peg2 peg from which disk could be taken or moved to
    *  @param acc the current accumulator (state) of the 'board'
    *  @return the disk to be moved, the 'from' peg, and the 'to' peg,
    *          if it is possible, otherwise returns None.
    */
  def findLegalMove(peg1: Peg, peg2: Peg, acc: Accumulator): Option[(Disk, Peg, Peg)] = {
    (acc.state(peg1).headOption, acc.state(peg2).headOption) match {

      case (Some(disk1), Some(disk2)) => if (disk1 < disk2) {
        Some((disk1, peg1, peg2))
      } else {
        Some((disk2, peg2, peg1))
      }

      case (Some(disk1), None) => Some((disk1, peg1, peg2))

      case (None, Some(disk2)) => Some((disk2, peg2, peg1))

      case (None, None) => None
    }
  }

  /** Wrapper function that pipes the next legal move into the move function
    *
    * @param peg1 first peg to be involved in disk move
    * @param peg2 second peg to be involved in disk move
    * @param accum the current state of the game
    * @return a new accumulator, if a move is found, otherwise None
    */
  def nextMove(peg1: Peg, peg2: Peg, accum: Accumulator): Option[Accumulator] = {
    findLegalMove(peg1, peg2, accum) match {
      case Some((disk, from, to)) => Some(movefn(disk, from, to, accum))
      case None => None
    }
  }

  def gameIsComplete(acc: Accumulator, end: Peg, n: Int): Boolean = {
    acc.state(end).length == n
  }

}