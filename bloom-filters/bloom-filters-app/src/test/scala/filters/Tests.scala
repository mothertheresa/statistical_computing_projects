package filters

import domain.StringBloomFilter
import RunnerUtilityFunctions._
import org.scalatest._
import filters.TestsUtility._

class RunnerSpec extends FlatSpec with Matchers {

  val sampleItem = "theresa"
  val sampleSet = Seq("hello", "world", "its", "me", "theresa", "does", "anything", "change",
    "if", "I", "add", "more", "items", "what", "happens")

  val numberOfBits = 100
  val numberOfHashes = 3

  "The parseArguments function" should "throw an error when it's missing arguments" in {
    assertThrows[IllegalArgumentException] {
      parseArguments(Array("numberOfBitsButNoHashes"))
    }
  }

  "The parseArguments function" should "throw an error when its arguments aren't Int" in {
    assertThrows[IllegalArgumentException] {
      parseArguments(Array("100", "notAnInt"))
    }
  }

  "A BloomFilter" should "have empty data upon instantiation" in {
    val filter: StringBloomFilter = new StringBloomFilter(numberOfBits, numberOfHashes)
    val entriesAllFalse: Boolean = filter.data.forall(p => !p)
    entriesAllFalse shouldEqual true
  }

  "A BloomFilter" should "have non-empty data once an item is inserted" in {
    val filter: StringBloomFilter = new StringBloomFilter(numberOfBits, numberOfHashes)
    filter.insert(sampleItem)
    val entriesAllFalse: Boolean = filter.data.forall(p => !p)
    entriesAllFalse shouldEqual false
  }

  "A BloomFilter" should "be able to contain multiple items" in {
    val filter: StringBloomFilter = new StringBloomFilter(numberOfBits, numberOfHashes)
    sampleSet.foreach(filter.insert)
    val entriesAllFalse: Boolean = filter.data.forall(p => !p)
    entriesAllFalse shouldEqual false
  }

  "A BloomFilter" should "return false often if it does not contain the item" in {
    val filter: StringBloomFilter = new StringBloomFilter(numberOfBits, numberOfHashes)
    filter.insert(sampleItem)
    filter.isElement("mark") shouldEqual false
  }

  "A BloomFilter" should "return true if it does contain the item" in {
    val filter: StringBloomFilter = new StringBloomFilter(numberOfBits, numberOfHashes)
    filter.insert(sampleItem)
    filter.isElement(sampleItem) shouldEqual true
  }

  "A BloomFilter containing multiple items" should "return true if it does contain the item" in {
    val filter: StringBloomFilter = new StringBloomFilter(numberOfBits, numberOfHashes)
    sampleSet.foreach(filter.insert)
    filter.isElement(sampleItem) shouldEqual true
  }

  "A BloomFilter containing multiple items" should "return false often if it does not contain the item" in {
    val filter: StringBloomFilter = new StringBloomFilter(numberOfBits, numberOfHashes)
    sampleSet.foreach(filter.insert)
    val empiricalProb = empiricalFalsePositive(filter, "mark")
    val theoreticalProb = falsePositiveProbability(numberOfHashes, numberOfBits, sampleSet.length)
    val diff = empiricalProb - theoreticalProb
    diff shouldBe < (1e-6)
  }

}