package lectures.session2.part_1_higher_order_functions

object Part1_HOF extends App {
  val list = List(0, 1, 2, 3, 4, 6, 7, 8, 9)

  val byTwo: List[Int] = list.map(i => i * 2)
  println(s"All values multiplied by two are = $byTwo")

  val onlyOdd: List[Int] = list.filter(i => i % 2 != 0)
  println(s"The odd numbers in the list are = $onlyOdd")

  val x = list.foldLeft("")((str, n) => s"$n$str$n")
  println(s"x is $x")

  val totalSum: Int = list.reduce((a, b) => a + b)
  println(s"The sum of all elements is = $totalSum")
}
