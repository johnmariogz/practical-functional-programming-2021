package lectures.session2.part_4_recursion

object Part2_RecursionPatternMatching extends App {
  // Build a recursive function that sums the elements of a list
  // TODO use pattern matching
  def sumList(list: List[Int]): Int = {
    list match {
      case Nil          => 0
      case head :: tail => head + sumList(tail)
    }
  }

  val list: List[Int] = List(1, 2, 3)
  val sum: Int        = sumList(list)
  println(s"The sum of $list is $sum")
}
