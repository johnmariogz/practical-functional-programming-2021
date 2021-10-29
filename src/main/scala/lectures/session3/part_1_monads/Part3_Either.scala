package lectures.session3.part_1_monads

object Part3_Either extends App {
  // TODO: Return an Int IFF the number is composed of [0-9]+
  def extractNumber(string: String): Either[String, Int] = {
    if (string.length != 0 && string.forall(c => c.isDigit)) {
      Right(string.toInt)
    } else {
      Left(s"'$string' cannot be parsed, sowwy!")
    }
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
  // TODO: If the number is even, say "$number cannot be operated with"
  def doSomethingWithTheNumber(t: Either[String, Int]): Either[String, Int] = {
    t.flatMap { x =>
      if (x % 2 != 0)
        Right(x + 1)
      else
        Left(s"$x cannot be operated with")
    }
  }

  val resultOdd = doSomethingWithTheNumber(resultNumber)
  println(s"After operating with '$resultNumber' the result is $resultOdd")

  val evenInput  = Right(2)
  val resultEven = doSomethingWithTheNumber(evenInput)
  println(s"After operating with '$evenInput' the result is $resultEven")
}
