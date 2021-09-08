package berlin.code.functional
package session2.recursion

object Part2_RecursionPatternMatching extends App {
  // Build a recursive function that sums the elements of a list
  // TODO use pattern matching
  def sumList(list: List[Int]): Int = {
    if (list.isEmpty) 0
    else list.head + sumList(list.tail)
  }

  val list = List(1, 2, 3)
  val sum  = sumList(list)
  println(s"The sum of $list is $sum")
}
