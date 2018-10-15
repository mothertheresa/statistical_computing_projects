package domain

/** Interface for bloom filter containing items of arbitrary type T
  *
  *  @tparam T the type of the items in the filter
  *  @param numberOfBits bits to use in filter storage
  *  @param numberOfHashes hashes to use in inserting and evaluating items in filter
  */
abstract class BloomFilter[T](numberOfBits: Int, numberOfHashes: Int) {
  def getNumberOfBits: Int = numberOfBits
  def getNumberofHashes: Int = numberOfHashes

  /** Inserts item into bloom filter
    *
    *  @param item item to be inserted
    *  @return in-place operation, truly functional would return BloomFilter[T]
    */
  def insert(item: T): Unit

  /** Identifies whether item is in bloom filter
    *
    *  @param item item to be checked
    *  @return whether or not the item is in the bloom filter,
    *          false is always right, but true is only sometimes right
    */
  def isElement(item: T): Boolean
}

/** Bloom filter containing items of type String
  *
  *  @constructor creates a new bloom filter given number of bits and hashes to use
  *  @param numberOfBits bits to use in filter storage
  *  @param numberOfHashes hashes to use in inserting and evaluating items in filter
  */
class StringBloomFilter(numberOfBits: Int, numberOfHashes: Int)
  extends BloomFilter[String](numberOfBits, numberOfHashes) {

  def insert(item: String): Unit = {
    val indices = evaluateItem(item)
    for(i <- indices)
      data(i) = true
  }

  def isElement(item: String): Boolean = {
    val indices = evaluateItem(item)
    indices.map(data(_)).forall(identity)
  }

  var data: Array[Boolean] = Array.ofDim[Boolean](numberOfBits) // should be private, leave like this for debugging

  private def randomHashFunctions: Seq[(String => Int)] = {
    Seq.range(0, numberOfHashes).map { seed =>
      (s: String) =>
        val hash = util.hashing.MurmurHash3.stringHash(str = s, seed = seed)
        math.abs(hash) % numberOfBits // remove sign and scale to numberOfHashes
    }
  }

  private def evaluateItem(item: String): Seq[Int] = {
    randomHashFunctions.map(hash => hash(item))
  }
}