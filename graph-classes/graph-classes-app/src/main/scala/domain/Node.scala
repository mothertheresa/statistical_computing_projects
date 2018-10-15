package domain

case class Node(id: Int, value: Option[Any]) {
  // you could also use a type class here to specify Node[String] or Node[Int]

  override def equals(that: Any): Boolean =
    that match {
      case Node(i, _) => id == i
      case _ => false
    }

}