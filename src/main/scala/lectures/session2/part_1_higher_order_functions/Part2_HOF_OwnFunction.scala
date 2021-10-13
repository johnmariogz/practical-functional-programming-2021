package lectures.session2.part_1_higher_order_functions

object Part2_HOF_OwnFunction extends App {

  def applyFunction(number: Int, function: Int => String): String = {
    val result: String = function(number)

    s"Input was $number and after using HOF the result was $result"
  }

  def invertInteger(i: Int): String = i.toString.reverse

  val executionOne = applyFunction(20, invertInteger)
  println(s"First execution is: $executionOne")

  def duplicateInteger(i: Int): String = s"$i$i"

  val executionTwo = applyFunction(30, duplicateInteger)
  println(s"Second execution is: $executionTwo")
}
