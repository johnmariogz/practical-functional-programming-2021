package lectures.session3.part_1_monads

import scala.util.Try

object Part2_Try extends App {
  // TODO: Return an Int IFF the number is composed of [0-9]+
  def extractNumber(string: String): Try[Int] = {
    Try(string.toInt)
  }

//  val emptyInput  = Helpers.readInput("empty")
//  val resultEmpty = extractNumber(emptyInput)
//  println(s"The result of '$emptyInput' is $resultEmpty")

  val numberInput  = Helpers.readInput("odd")
  val resultNumber = extractNumber(numberInput)
  println(s"The result of '$numberInput' is $resultNumber")

//  val mixedInput  = Helpers.readInput("mixed")
//  val resultMixed = extractNumber(mixedInput)
//  println(s"The result of '$mixedInput' is $resultMixed")

  // TODO: If the number is odd, add 1 to it and return it
  // TODO: If the number is even, ignore it
  def doSomethingWithTheNumber(t: Try[Int]): Try[Int] = {
    t.filter(i => i % 2 != 0).map(_ + 1)
  }

  val resultOdd = doSomethingWithTheNumber(resultNumber)
  println(s"After operating with '$resultNumber' the result is $resultOdd")

  val evenInput  = Try(2)
  val resultEven = doSomethingWithTheNumber(evenInput)
  println(s"After operating with '$evenInput' the result is $resultEven")
}
