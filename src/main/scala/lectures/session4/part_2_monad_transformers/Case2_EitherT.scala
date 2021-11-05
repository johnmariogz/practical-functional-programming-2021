package lectures.session4.part_2_monad_transformers

import exercises.session3.FakeBlog
import exercises.session3.FakeBlog._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Case2_EitherT extends App {
  val blog = FakeBlog

  def getUser(id: String): Future[Either[String, User]] = {
    val userId = UserId(id)
    blog.getUser(userId).flatMap {
      case Some(user) =>
        Future.successful(Right(user))
      case None =>
        Future.successful(Left(s"User with id=$userId is not in the database"))
    }
  }

  def calculateUserScore(id: String): Future[Either[String, Score]] = {
    ???
  }

  def printUserScore(id: String): Unit = {
    val score: Either[String, Score] = Await.result(calculateUserScore(id), 1.second)
    println(score)
  }

  printUserScore("user1") // Prints Right(user1, 3.5d)
  printUserScore("user4") // Prints Right(user4, 0.0)
  printUserScore(
    "non-existing"
  ) // Prints 'User with id=non-existing is not in the database'
}
