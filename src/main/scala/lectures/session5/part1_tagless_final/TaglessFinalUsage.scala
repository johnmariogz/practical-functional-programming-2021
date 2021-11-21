package lectures.session5.part1_tagless_final

import cats.Monad
import cats.data.OptionT
import cats.effect.{IO, unsafe}
import exercises.shared.FakeBlog._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object TaglessFinalUsage extends App {
  sealed abstract class Repository[F[_]: Monad] {
    def getUser(userId: UserId): F[Option[User]]
    def getAllUsers(): F[List[User]]
    def getBlogPosts(user: User): F[List[BlogPost]]
    def getComments(user: User): F[List[Comment]]

    final def calculateUserScore(userId: UserId): F[Option[Score]] = {
      val transformer: OptionT[F, Score] = for {
        user     <- OptionT(getUser(userId))
        comments <- OptionT.liftF(getComments(user))
        posts    <- OptionT.liftF(getBlogPosts(user))
      } yield Score(user.id, posts.size * 1.0 + comments.size * 0.1)

      transformer.value
    }
  }

  object IORepository extends Repository[IO] {
    override def getUser(userId: UserId): IO[Option[User]] =
      ???

    override def getAllUsers(): IO[List[User]] =
      ???

    override def getBlogPosts(user: User): IO[List[BlogPost]] =
      ???

    override def getComments(user: User): IO[List[Comment]] =
      ???
  }

  object FutureRepository extends Repository[Future] {
    def getUser(userId: UserId): Future[Option[User]] =
      ???

    def getAllUsers(): Future[List[User]] =
      ???

    def getBlogPosts(user: User): Future[List[BlogPost]] =
      ???

    def getComments(user: User): Future[List[Comment]] =
      ???
  }

  def printExecution(name: String, f: UserId => Option[Score]): Unit = {
    val userIds = List(
      "user1",
      "user2",
      "user100"
    )

    println(s"===[Start] $name ===")
    userIds.foreach { userId =>
      val result = f(UserId(userId))
      println(s"\t$userId = $result")
    }
    println(s"===[End] $name ===")
  }

  def printFuture(): Unit =
    printExecution(
      "Future",
      userId => Await.result(FutureRepository.calculateUserScore(userId), 1.second)
    )

  def printIO(): Unit =
    printExecution(
      "IO",
      userId => IORepository.calculateUserScore(userId).unsafeRunSync()(unsafe.implicits.global)
    )

  printFuture()
  printIO()
}
