package exercises.session2

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RecursionTest extends AnyFlatSpec with Matchers {
  "#factorialLength" should "return 3 for 5!" in {
    Recursion.factorialLength(5) shouldBe 3
  }

  "#factorialLength" should "return 3 for 6!" in {
    Recursion.factorialLength(6) shouldBe 3
  }

  // TODO: Write extra tests. What happens with 50! and 100!
}
