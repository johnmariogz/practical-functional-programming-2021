package exercises.session2

object Containers {
  // Extract all elements from a list
  // If at least one of the elements is None, then None is returned
  // Example 1: List(Some(1), Some(2)) => Some(List(1, 2))
  // Example 2: List(Some(1), Some(2), None) => None
  def simplifyList(list: List[Option[Int]]): Option[List[Int]] = {
    val reducedList = list.flatten
    if (reducedList.size != list.size) None
    else Some(reducedList)
  }
}
