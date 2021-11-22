package lectures.session5.part4_database

import java.time.Instant
import java.util.UUID

object Database {
  case class UserId(id: UUID) extends AnyVal
  case class User(
      id: UserId,
      name: String,
      createdAt: Instant,
      modifiedAt: Instant
  )
  object User {
    def apply(name: String, createdAt: Instant, modifiedAt: Instant): User =
      User(
        id = UserId(UUID.randomUUID()),
        name = name,
        createdAt = createdAt,
        modifiedAt = modifiedAt
      )
  }

  case class PostId(id: UUID) extends AnyVal
  case class Post(
      id: PostId,
      authorId: UserId,
      content: String,
      createdAt: Instant,
      modifiedAt: Instant
  )
  object Post {
    def apply(authorId: UserId, content: String, createdAt: Instant, modifiedAt: Instant): Post =
      Post(
        id = PostId(UUID.randomUUID()),
        authorId = authorId,
        content = content,
        createdAt = createdAt,
        modifiedAt = modifiedAt
      )
  }

  case class CommentId(id: UUID) extends AnyVal
  case class Comment(
      id: CommentId,
      authorId: UserId,
      postId: PostId,
      comment: String,
      createdAt: Instant,
      modifiedAt: Instant
  )
  object Comment {
    def apply(
        authorId: UserId,
        postId: PostId,
        comment: String,
        createdAt: Instant,
        modifiedAt: Instant
    ): Comment = Comment(
      id = CommentId(UUID.randomUUID()),
      authorId = authorId,
      postId = postId,
      comment = comment,
      createdAt = createdAt,
      modifiedAt = modifiedAt
    )
  }
}
