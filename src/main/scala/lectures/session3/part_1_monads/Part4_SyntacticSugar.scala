package lectures.session3.part_1_monads

object Part4_SyntacticSugar extends App {
  private val users = List(
    User("123", 19),
    User("456", 26)
  )
    .map(u => u.age -> u)
    .toMap

  private val posts = Map(
    "456" -> List(
      Post(1, "456", "My first post"),
      Post(2, "456", "Something else I liked")
    )
  )

  case class User(id: String, age: Int)
  case class Post(id: Int, userId: String, text: String)
  case class UserWithPosts(user: User, posts: List[Post])

  def findUserByAge(age: Int): Option[User]             = users.get(age)
  def findUserPosts(userId: String): Option[List[Post]] = posts.get(userId)

  // TODO: Build a function that returns ONLY users of that age that HAVE posts
  def findUserOfAgeWithPosts(age: Int): Option[UserWithPosts] = {
    for {
      user <- findUserByAge(age)
      posts <- findUserPosts(user.id)
    } yield UserWithPosts(user, posts)
  }

  val result19 = findUserOfAgeWithPosts(19)
  println(s"For age 19 the result is $result19")

  val result26 = findUserOfAgeWithPosts(26)
  println(s"For age 26 the result is $result26")
}
