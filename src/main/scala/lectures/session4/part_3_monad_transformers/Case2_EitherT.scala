package lectures.session4.part_3_monad_transformers

import cats.data.EitherT
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
    val transformer: EitherT[Future, String, Score] = for {
      user     <- EitherT(getUser(id))
      posts    <- EitherT.liftF(blog.getBlogPosts(user))
      comments <- EitherT.liftF(blog.getComments(user))
      // EitherT(Left)
    } yield Score(user.id, posts.size * 1.0 + comments.size * 0.1)

    transformer.value
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
