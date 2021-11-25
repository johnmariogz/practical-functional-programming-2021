package lectures.session5.part2_http_usage

import cats.effect._
import exercises.shared.FakeBlog.UserId
import io.circe.generic.auto._
import lectures.session5.part1_tagless_final.TaglessFinalUsage.IORepository
import org.http4s.HttpRoutes
import org.http4s.Method.POST
import org.http4s.blaze.server
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.io._

import scala.concurrent.ExecutionContext.Implicits.global

object HttpUsage extends App {
  val repository = IORepository

  val app = HttpRoutes
    .of[IO] {
      case req @ POST -> Root / "user-score" =>
        for {
          userId   <- req.as[UserId]
          result   <- repository.calculateUserScore(userId)
          response <- Ok(result)
        } yield response

        // GET /users/:id => user :)
      case _@ GET -> Root / "users" / UserId(userId) =>
        for {
          user <- repository.getUser(userId)
          response <- user match {
            case Some(actualUser) =>
              Ok(actualUser)

            case None =>
              NotFound()
          }
        } yield response

        // GET /users/{id}/posts
      case _@ GET -> Root / "users" / UserId(userId) / "posts"  =>
        for {
          user <- repository.getUser(userId)
          response <- user match {
            case Some(actualUser) =>
              for {
                posts <- repository.getBlogPosts(actualUser)
                response <- Ok(posts)
              } yield response

            case None =>
              Gone()
          }
        } yield response

    // TODO Implement endpoints to return user, comments and posts
    }
    .orNotFound

  val appServer = server
    .BlazeServerBuilder[IO]
    .withExecutionContext(global)
    .bindHttp(8080)
    .withHttpApp(app)
    .serve
    .compile
    .drain
    .as(ExitCode.Success)

  // This is typically provided by IOApp
  implicit val runtime: cats.effect.unsafe.IORuntime = cats.effect.unsafe.IORuntime.global

  appServer.unsafeRunSync()
}
