package lectures.session2.part_4_recursion

object Part1_RecursionNaive extends App {
  // Build a recursive function that sums the elements of a list
  // TODO use if-else
  def sumList(list: List[Int]): Int = {
    if (list.isEmpty) 0
    else list.head + sumList(list.tail)
  }

  val list: List[Int] = List(1, 2, 3)
  val sum: Int        = sumList(list)
  println(s"The sum of $list is $sum")
}
