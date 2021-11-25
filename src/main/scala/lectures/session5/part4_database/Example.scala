package lectures.session5.part4_database

import cats.effect.unsafe.implicits.global
import cats.effect.{Clock, IO}
import cats.free.Free
import doobie._
import doobie.free.connection
import doobie.implicits._
import lectures.session5.part4_database.Database._

object Example extends App with DatabaseStart with DatabaseBoilerplate {
  val application = for {
    db         <- startDatabase()
    _          <- applyMigrations(db)
    transactor <- buildTransactor(db)
    userNames  <- IO.pure(List("Yoav", "Alexey", "Chris"))
    _          <- executeDatabaseQueries(userNames).transact(transactor)
  } yield ()

  def executeDatabaseQueries(names: List[String]): Free[connection.ConnectionOp, Unit] = {

    def findAllUsers(): ConnectionIO[List[User]] =
      sql"select id, name, created_at, modified_at from users".query[User].to[List]

    def findAllPosts(): ConnectionIO[List[Post]] =
      sql"select id, author_id, content, created_at, modified_at from posts".query[Post].to[List]

    def findAllCommentsFromUser(userId: UserId): ConnectionIO[List[Comment]] =
      sql"select id, author_id, post_id, comment, created_at, modified_at from comments where author_id = $userId::uuid"
        .query[Comment]
        .to[List]

    def findAllPostsFromUser(userId: UserId): ConnectionIO[List[Post]] =
      sql"select id, author_id, content, created_at, modified_at from posts where author_id = $userId::uuid"
        .query[Post]
        .to[List]

    def storeUsers(names: List[String]): ConnectionIO[Int] = {
      val query =
        "insert into users (id, name, created_at, modified_at) values (?::uuid, ?, ?::timestamp, ?::timestamp)"

      for {
        now <- Clock[ConnectionIO].realTimeInstant
        users <- connection.pure {
          names.map { name =>
            User(
              name = name,
              createdAt = now,
              modifiedAt = now
            )
          }
        }
        result <- Update[User](query).updateMany(users)
      } yield result
    }

    def storePosts(users: List[User]): ConnectionIO[Int] = {
      val query = """
       |insert into posts
       |(id, author_id, content, created_at, modified_at)
       | values (?::uuid, ?::uuid, ?, ?::timestamp, ?::timestamp)
       |""".stripMargin

      for {
        now <- Clock[ConnectionIO].realTimeInstant
        posts <- connection.pure {
          users.map { user =>
            Post(
              authorId = user.id,
              content = s"My name is ${user.name} and this is my first post",
              createdAt = now,
              modifiedAt = now
            )
          }
        }
        result <- Update[Post](query).updateMany(posts)
      } yield result
    }

    def storeComments(
        users: List[User],
        posts: List[Post]
    ): Free[connection.ConnectionOp, Int] = {
      val query =
        """
          |insert into comments
          | values (?::uuid, ?::uuid, ?::uuid, ?, ?::timestamp, ?::timestamp)
          |""".stripMargin
      for {
        now <- Clock[ConnectionIO].realTimeInstant
        comments <- connection.pure {
          posts.flatMap { post =>
            users.filterNot(_.id == post.authorId).map { user =>
              Comment(
                authorId = user.id,
                postId = post.id,
                comment = s"I am ${user.name} and I like your post '${post.content}'",
                createdAt = now,
                modifiedAt = now
              )
            }
          }
        }
        result <- Update[Comment](query).updateMany(comments)
      } yield result
    }

    def print[A](a: A): Free[connection.ConnectionOp, Unit] =
      connection.delay(println(a))

    for {
      _                     <- storeUsers(names)
      allUsers              <- findAllUsers()
      _                     <- storePosts(allUsers)
      allPosts              <- findAllPosts()
      _                     <- storeComments(allUsers, allPosts)
      _                     <- print("= All users =")
      _                     <- print(allUsers)
      _                     <- print("= All posts =")
      _                     <- print(allPosts)
      postsFromLastUser     <- findAllPostsFromUser(allUsers.last.id)
      _                     <- print("= Posts from last user =")
      _                     <- print(postsFromLastUser)
      _                     <- print("= Comments from first user =")
      commentsFromFirstUser <- findAllCommentsFromUser(allUsers.head.id)
      _                     <- print(commentsFromFirstUser)
    } yield ()
  }

  application.unsafeRunSync()
}
