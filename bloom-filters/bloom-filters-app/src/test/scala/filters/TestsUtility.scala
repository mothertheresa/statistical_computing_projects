package filters

import domain.StringBloomFilter

object TestsUtility {

  /** Calculates theoretical false positive probability rate of bloom filter
    *
    *  @param k number of hash functions in filter
    *  @param m number of bits in filter
    *  @param n number of items inserted into filter
    *  @return theoretical false positive probability given by Wikipedia,
    *          which assumes independence so it will tend to overestimate.
    */
  def falsePositiveProbability(k: Int, m: Int, n: Int): Double = {
    math.pow(1 - math.pow(1 - (1.0 / m.toDouble), k * n), k)
  }

  /** Calculates empirical false positive probability rate of String bloom filter
    *
    *  @param filter String bloom filter
    *  @param item item to check
    *  @param n number of attempts, defaults to 1000
    *  @return empirical false positive probability,
    *          counts number of times false classification occurs over total.
    */
  def empiricalFalsePositive(filter: StringBloomFilter, item: String, n: Int = 1000): Double = {
    val wrongCount = Seq.range(0, n).map(_ => filter.isElement(item)).count(identity)
    wrongCount.toDouble / n.toDouble
  }

  def triesUntilFalse(filter: StringBloomFilter, item: String): Int = {
    var count = 0
    var wrongResult = true

    while ( {
      wrongResult && (count < 1000)
    }) {
      wrongResult = filter.isElement(item)
      count = count + 1
    }

    count
  }

}