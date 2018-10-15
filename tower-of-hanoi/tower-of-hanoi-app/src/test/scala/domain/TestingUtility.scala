package domain

object TestingUtility {

  def minimumNumberOfMoves(numberOfDisks: Int): Int = {
    if (numberOfDisks < 3) {
      throw new IllegalArgumentException("Number of disks must be at least 3.")
    }

    math.pow(2, numberOfDisks).toInt - 1
  }

}