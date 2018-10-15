package domain

import domain.TowerOfHanoiTypes._
import domain.TowerOfHanoiUtility._

object TowerOfHanoiRecursive {

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

    n match {

      case 1 => movefn(0, start, end, acc) // base case to move smallest disk to destination

      case _ =>
        // (1) first move the n-1 tower from start to extra
        var newAcc = hanoi(n-1, start, end, extra, movefn, acc)

        // (2) next move the largest disk (which has id n-1) to the final destination
        newAcc = movefn(n-1, start, end, newAcc)

        // (3) finally move the n-1 tower of disks on top at the final destination
        hanoi(n-1, extra, start, end, movefn, newAcc)

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

}