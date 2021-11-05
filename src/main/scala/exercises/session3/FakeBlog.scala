package exercises.session3

import scala.concurrent.Future

object FakeBlog {
  case class UserId(id: String) extends AnyVal
  case class User(id: UserId, authorName: String)
  case class BlogPostId(id: Int)
  case class BlogPost(id: BlogPostId, userId: UserId, title: String, text: String)
  case class CommentId(id: Int)
  case class Comment(id: CommentId, blogPostId: BlogPostId, userId: UserId, text: String)
  case class Score(userId: UserId, score: Double)

  private[session3] val user1 = User(UserId("user1"), "authorName1")
  private[session3] val user2 = User(UserId("user2"), "authorName2")
  private[session3] val user3 = User(UserId("user3"), "authorName3")
  private[session3] val user4 = User(UserId("user4"), "authorName3")

  private val users: Map[UserId, User] = Map(
    user1.id -> user1,
    user2.id -> user2,
    user3.id -> user3,
    user4.id -> user4
  )

  private val post1 = BlogPost(BlogPostId(1), user1.id, "title1", "content1")
  private val post2 = BlogPost(BlogPostId(2), user1.id, "title2", "content2")
  private val post3 = BlogPost(BlogPostId(3), user2.id, "title3", "content3")
  private val post4 = BlogPost(BlogPostId(4), user1.id, "title4", "content4")

  private val blogPostsPerUser: Map[UserId, List[BlogPost]] = List(
    post1,
    post2,
    post3,
    post4
  ).groupBy(_.userId)

  private val comment1 = Comment(CommentId(1), post1.id, user1.id, "comment1")
  private val comment2 = Comment(CommentId(2), post1.id, user1.id, "comment2")
  private val comment3 = Comment(CommentId(3), post2.id, user2.id, "comment3")
  private val comment4 = Comment(CommentId(4), post1.id, user2.id, "comment4")
  private val comment5 = Comment(CommentId(5), post1.id, user1.id, "comment5")
  private val comment6 = Comment(CommentId(6), post1.id, user1.id, "comment6")
  private val comment7 = Comment(CommentId(7), post1.id, user2.id, "comment7")
  private val comment8 = Comment(CommentId(8), post1.id, user1.id, "comment8")

  private val commentsPerBlogPost: Map[UserId, List[Comment]] = List(
    comment1,
    comment2,
    comment3,
    comment4,
    comment5,
    comment6,
    comment7,
    comment8
  )
    .groupBy(_.userId)

  // TODO What if the user is NOT present?
  def getUser(userId: UserId): Future[Option[User]] =
    Future.successful(users.get(userId))

  def getBlogPosts(user: User): Future[List[BlogPost]] =
    Future.successful(blogPostsPerUser.getOrElse(user.id, Nil))

  def getComments(user: User): Future[List[Comment]] =
    Future.successful(commentsPerBlogPost.getOrElse(user.id, Nil))

  def getAllBlogPosts(): Future[List[BlogPost]] =
    Future.successful(blogPostsPerUser.values.flatten.toList)

  def getAllComments(): Future[List[Comment]] =
    Future.successful(commentsPerBlogPost.values.flatten.toList)
}
