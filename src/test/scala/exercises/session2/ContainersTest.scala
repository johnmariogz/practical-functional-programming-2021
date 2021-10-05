package exercises.session2

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ContainersTest extends AnyFlatSpec with Matchers {
  "#simplifyList" should "return Some(List(1,2,))" in {
    Containers.simplifyList(List(Some(1), Some(2))) shouldBe Some(List(1, 2))
  }

  "#simplifyList" should "return None" in {
    Containers.simplifyList(List(Some(1), Some(2), None)) shouldBe None
  }

  "#simplifyList" should "return Some(Nil)" in {
    Containers.simplifyList(Nil) shouldBe Some(Nil)
  }
}
