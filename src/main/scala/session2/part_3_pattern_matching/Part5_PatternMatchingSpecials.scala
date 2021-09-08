package berlin.code.functional
package session2.part_3_pattern_matching

import scala.util.{Failure, Success, Try}

object Part5_PatternMatchingSpecials extends App {
  val optionExampleOne: Option[Int] = Some(3)
  optionExampleOne match {
    case Some(value) => println(s"Value contained in 'optionExampleOne' is $value")
    case None        => // This will not be executed
  }

  val optionExampleTwo: Option[Int] = None
  optionExampleTwo match {
    case Some(_) => // This will never be executed
    case None    => println("'optionExampleTwo' is empty!!")
  }

  println()

  val successfulOperation: Try[Int] = Try(42) // A dangerous operation
  successfulOperation match {
    case Failure(_)     => // This will not be executed
    case Success(value) => println(s"For a dangerous operation we got $value")
  }

  val dangerousCalculation: Int = 10 & 0                         // This tricks the compiler
  val failedOperation: Try[Int] = Try(42 / dangerousCalculation) // An even more dangerous operation
  failedOperation match {
    case Failure(exception) =>
      println(s"The dangerous operation failed badly! '${exception.getMessage}'")
    case Success(_) => // This will not be executed
  }

  println()

  val rightOp: Either[String, Int] = Right(42)
  rightOp match {
    case Left(_)      => // This will not be executed
    case Right(value) => println(s"Here is Right and the content is $value")
  }

  val leftOp: Either[String, Int] = Left("This went to the left")
  leftOp match {
    case Left(value) => println(s"Left had a message: $value")
    case Right(_)    => // This will not be executed
  }
}
