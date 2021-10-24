package exercises.session3

import exercises.session3.FakeBlog._

import scala.concurrent.Future

object Exercises {

  /** Compute the user score = (Sum of comment scores + post scores)
    * Every comment = 0.1d
    * Every post = 1.0d
    *
    * Use the following functions already defined:
    * getUser(userId: UserId): Future[User]
    * getBlogPosts(user: User): Future[List[BlogPost]]
    * getComments(user: User): Future[List[Comment]]
    *
    * Think on how to do the operations in a parallel manner
    */
  def calculateUserScore(id: String): Future[Score] = ???

  /** Return the 2-tuple of user and how many blog posts the author has written
    *
    * Use the following functions in parallel
    * getAllBlogPosts(): Future[List[BlogPost]]
    * getUser(userId: UserId): Future[User]
    * Use parallel computations for a faster search
    */
  def countPostsPerAuthor(): Future[List[(User, Int)]] = ???

  /** Return the 2-tuple of user and how many comments the author has written
    *
    * getUser(userId: UserId): Future[User]
    * def getAllComments(): Future[List[Comment]]
    * Use parallel computations for a faster search
    * @return
    */
  def countCommentsPerAuthor(): Future[List[(User, Int)]] = ???
}
