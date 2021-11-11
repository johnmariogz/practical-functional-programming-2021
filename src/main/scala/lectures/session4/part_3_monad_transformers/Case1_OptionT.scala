package lectures.session4.part_3_monad_transformers

import cats.implicits._
import cats.data.OptionT
import exercises.session3.FakeBlog
import exercises.session3.FakeBlog._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Case1_OptionT extends App {
  val blog = FakeBlog

  // TODO Use a monad transformer to calculate the user score
  def calculateUserScore(id: String): Future[Option[Score]] = {
    val userId = UserId(id)
    val transformer: OptionT[Future, Score] = for {
      user     <- OptionT(blog.getUser(userId))
      comments <- OptionT.liftF(blog.getComments(user))
      // - OptionT(None)
      posts <- OptionT.liftF(blog.getBlogPosts(user))
    } yield Score(user.id, posts.size * 1.0 + comments.size * 0.1)

    transformer.value
  }

  def printUserScore(id: String): Unit = {
    val score: Option[Score] = Await.result(calculateUserScore(id), 1.second)
    println(score)
  }

  printUserScore("user1")        // Prints Some(user1, 3.5d)
  printUserScore("user4")        // Prints Some(user4, 0.0)
  printUserScore("non-existing") // Prints None - no user has this id
}
