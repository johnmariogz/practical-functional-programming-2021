package berlin.code.functional
package session2.part_1_higher_order_functions

object Part2_HOF_OwnFunction extends App {

  def applyFunction(number: Int, function: Int => String): String = {
    val result: String = function(number)

    s"Input was $number and after using HOF the result was $result"
  }

  val executionOne = applyFunction(20, ???) // TODO Make it invert the number (e.g. 10 => 01)
  println(s"First execution is: $executionOne")

  val executionTwo = applyFunction(30, ???) // TODO Make it Duplicate the string (e.g. 11 => 1111)
  println(s"Second execution is: $executionTwo")
}
