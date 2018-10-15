package domain

trait IGraph[E <: Edge] {
  val edges: Set[E]
  val nodes: Set[Node]

  def addEdge(newEdge: E): IGraph[E] // functional design means here we want to return an IGraph, not Unit
  def addNode(newNode: Node): IGraph[E]

  def removeEdge(edge: E): IGraph[E]
  def removeNode(node: Node): IGraph[E]

  def edgesOfNode(node: Node): Set[E]

  def path(start: Node, end: Node): Seq[E] // notice here we have a Seq, which is ordered vs. unordered Set
}

trait IDirectedGraph extends IGraph[DirectedEdge]

trait IUndirectedGraph extends IGraph[UndirectedEdge]