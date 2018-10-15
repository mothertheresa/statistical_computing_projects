package domain

import TowerOfHanoiRecursive._
import TowerOfHanoiUtility._
import TestingUtility._
import org.scalatest._

class TowerOfHanoiTests extends FlatSpec with Matchers {

  val n = 5
  val start = "Start"
  val extra = "Extra"
  val end = "End"

  "The hanoi function" should "move all disks to the 'end' peg" in {
    val acc = initialState(n, start, extra, end)
    val numberOfDisksOnEnd = hanoi(n, start, extra, end, movefn, acc).state(end).length
    numberOfDisksOnEnd shouldEqual n
  }

  "The hanoi function" should "move all disks in the minimum number of moves required" in {
    val acc = initialState(n, start, extra, end)
    val numberOfMoves = hanoi(n, start, extra, end, movefn, acc).numberOfMoves
    numberOfMoves shouldEqual minimumNumberOfMoves(n)
  }

  "The movefn function" should "return a state where the disk is on the destination peg" in {
    val acc = initialState(n, start, extra, end)
    val diskOnDestinationPeg = movefn(0, start, end, acc).state(end).head
    diskOnDestinationPeg shouldEqual 0
  }

  "The movefn function" should "return a state where the disk is not on the source peg" in {
    val acc = initialState(n, start, extra, end)
    val diskOnSourcePeg = movefn(0, start, end, acc).state(start).head
    diskOnSourcePeg should not equal 0
  }

  "The movefn function" should "fail if you try to move a disk not at the top of the source peg" in {
    val acc = initialState(n, start, extra, end)
    an [IllegalArgumentException] should be thrownBy movefn(3, start, end, acc)
  }

  "The movefn function" should "fail if you try to move a disk to a nonexistent peg" in {
    val acc = initialState(n, start, extra, end)
    an [IllegalArgumentException] should be thrownBy movefn(0, start, "Fake", acc)
  }

  "The movefn function" should "fail if you try to move a disk illegally" in {
    var acc = initialState(n, start, extra, end)
    acc = movefn(0, start, end, acc)
    an [IllegalArgumentException] should be thrownBy movefn(1, start, end, acc)
  }

}