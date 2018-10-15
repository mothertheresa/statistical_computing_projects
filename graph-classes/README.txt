Welcome to graph classes!

(-) ensure you have Scala and SBT installed
(-) only one test was written to ensure equality between undirected edges, you can run this writing ‘sbt test’ from the graph-classes-app directory
(-) graph classes were implemented using Scala’s abstract type classes, which allowed me to specify the interface for an IGraph, and then let IDirectedGraph inherit from it using its DirectedEdge and IUndirectedGraph inherit from IGraph using its UndirectedEdge
(-) Node can contain a value (hence Option)
(-) Edge can contain a weight (hence Option)