package berlin.code.functional
package session2.recursion

object Part3_TailRecursion extends App {
  // Build a recursive function that sums the elements of a list
  // TODO Make it work for large lists (10.000 or more elements)
  def sumList(list: List[Int]): Int = {
    list match {
      case Nil          => 0
      case head :: tail => head + sumList(tail)
    }
  }

  val listSmall: List[Int] = List(1, 2, 3)
  val smallSum: Int        = sumList(listSmall)
  println(s"The sum of $listSmall is $smallSum")

  val listLarge: List[Int] = 1.to(10_000).toList
  val largeSum: Int        = sumList(listLarge)
  println(s"The sum of List(1, ..., 10_000) is $largeSum")
}
