package berlin.code.functional
package session2.part_2_partial_functions

object Part1_PartialllyAppliedFunctions extends App {
  //           f: Int => Int => Int
  def multiply(a: Int)(b: Int): Int = a * b

  // TODO Make all numbers multiply by 7
  val multiplyBy7: Int => Int = ???
  val valueOf3                = multiplyBy7(3)
  println(s"Original was 3 and multiplied is $valueOf3")
  val valueOf7 = multiplyBy7(7)
  println(s"Original was 7 and multiplied is $valueOf7")

  //      f: (Int, Int) => Int
  def sum(a: Int, b: Int): Int = a + b

  // TODO Rewrite sum as a partially applied function and make it always add 42
  val sumAsPAF: Int => Int => Int = ???
  val sum42: Int => Int           = sumAsPAF(42)
  val valueOf66                   = sum42(66)
  println(s"Initial value of 66 and final of $valueOf66")
}
