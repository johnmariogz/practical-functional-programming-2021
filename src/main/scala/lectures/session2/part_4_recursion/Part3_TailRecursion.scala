package lectures.session2.part_4_recursion

import scala.annotation.tailrec

object Part3_TailRecursion extends App {
  // Build a recursive function that sums the elements of a list
  // TODO Make it work for large lists (10.000 or more elements)

  @tailrec
  def sumList(list: List[Int], acc: Int = 0): Int = {
    list match {
      case Nil          => acc
      case head :: tail => sumList(tail, head + acc)
    }
  }

  val listSmall: List[Int] = List(1, 2, 3)
  val smallSum: Int        = sumList(listSmall)
  println(s"The sum of $listSmall is $smallSum")

  val listLarge: List[Int] = 1.to(100_000).toList
  val largeSum: Int        = sumList(listLarge)
  println(s"The sum of List(1, ..., 100_000) is $largeSum")
}
