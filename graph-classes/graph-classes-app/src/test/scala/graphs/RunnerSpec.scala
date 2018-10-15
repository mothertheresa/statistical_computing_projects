package babbler

import domain.{Node, UndirectedEdge}
import org.scalatest._

class RunnerSpec extends FlatSpec with Matchers {

  "Two undirected edges with different orders" should "still be considered equal" in {
    val n1 = Node(1, None)
    val n2 = Node(2, None)

    val e1 = UndirectedEdge(n1, n2, None)
    val e2 = UndirectedEdge(n2, n1, None)

    e1.equals(e2) shouldEqual true
  }

}