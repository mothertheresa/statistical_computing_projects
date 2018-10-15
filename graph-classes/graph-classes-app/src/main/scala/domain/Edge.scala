package domain

trait Edge {
  val n1: Node
  val n2: Node
  val weight: Option[Double]
}

case class DirectedEdge(n1: Node, n2: Node, weight: Option[Double]) extends Edge

case class UndirectedEdge(n1: Node, n2: Node, weight: Option[Double]) extends Edge {

  override def equals(that: Any): Boolean = // need to override equals since order doesn't matter for undirected edges
    that match {
      case e: Edge => e.n1.equals(n1) && e.n2.equals(n2) || e.n1.equals(n2) && e.n2.equals(n1)
      case _ => false
    }

}