package exercises.session3

import exercises.shared.FakeBlog._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Exercises {

  /** Compute the user score = (Sum of comment scores + post scores)
    * Every comment = 0.1d
    * Every post = 1.0d
    *
    * Use the following functions already defined:
    * getUser(userId: UserId): Future[Option[User]]
    * getBlogPosts(user: User): Future[List[BlogPost]]
    * getComments(user: User): Future[List[Comment]]
    *
    * Think on how to do the operations in a parallel manner
    */
  def calculateUserScore(id: String): Future[Option[Score]] = {
    getUser(UserId(id)).flatMap {
      case Some(user) =>
        for {
          posts    <- getBlogPosts(user)
          comments <- getComments(user)
        } yield Some(Score(user.id, posts.size * 1.0 + comments.size * 0.1))

      case None =>
        Future.successful(None)
    }
  }

  /** Return the 2-tuple of user and how many blog posts the author has written
    *
    * Use the following functions in parallel
    * Use parallel computations for a faster search
    */
  def countPostsPerAuthor(): Future[List[(User, Int)]] =
    for {
      users <- getAllUsers()
      posts <- Future.traverse(users)(user => getBlogPosts(user).map(posts => (user, posts.size)))
    } yield posts

  /** Return the 2-tuple of user and how many comments the author has written
    *
    * Use parallel computations for a faster search
    * @return
    */
  def countCommentsPerAuthor(): Future[List[(User, Int)]] =
    for {
      users <- getAllUsers()
      comments <- Future.traverse(users)(user =>
        getComments(user).map(comments => (user, comments.size))
      )
    } yield comments
}
