package exercises.session2

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PAFTest extends AnyFlatSpec with Matchers {
  "#map" should "multiply each element by 3" in {
    PAF.map(List(1, 2, 3))(i => i * 3) shouldBe List(3, 6, 9)
  }

  "#map" should "return the same list" in {
    PAF.map(List(1, 2, 3))(x => x) shouldBe List(1, 2, 3)
  }

  "#filter" should "return strings with only digits" in {
    PAF.filter(List("a", "b", "1", "2", "c4"))(s => s.forall(_.isDigit)) shouldBe List("1", "2")
  }
}
