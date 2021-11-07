package exercises.session3

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ExercisesTest extends AnyFlatSpec with Matchers with ScalaFutures {
  import FakeBlog._
  import Exercises._

  "#calculateUserScore" should "calculate the score for user1" in {
    calculateUserScore("user1").futureValue shouldBe Some(Score(UserId("user1"), 3.5d))
  }

  "#calculateUserScore" should "calculate the score for user2" in {
    calculateUserScore("user2").futureValue shouldBe Some(Score(UserId("user2"), 1.3d))
  }

  "#calculateUserScore" should "calculate the score for user4" in {
    calculateUserScore("user4").futureValue shouldBe Some(Score(UserId("user4"), 0.0d))
  }

  "#calculateUserScore" should "calculate the score for not-present" in {
    calculateUserScore("not-present").futureValue shouldBe None
  }

  "#countPostsPerAuthor" should "count all the posts per author" in {
    countPostsPerAuthor().futureValue should contain theSameElementsAs List(
      (user1, 3),
      (user2, 1),
      (user3, 0),
      (user4, 0)
    )
  }

  "#countCommentsPerAuthor" should "count all the comments per author" in {
    countCommentsPerAuthor().futureValue should contain theSameElementsAs List(
      (user1, 5),
      (user2, 3),
      (user3, 0),
      (user4, 0)
    )
  }
}
