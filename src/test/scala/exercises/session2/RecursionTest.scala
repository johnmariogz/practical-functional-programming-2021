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

  "#factorialLength" should "return 158 for 100!" in {
    Recursion.factorialLength(100) shouldBe 158
  }

  "#factorialLength" should "return 2568 for 1000!" in {
    Recursion.factorialLength(1000) shouldBe 2568
  }

  "#factorialLength" should "return 35660 for 10000!" in {
    Recursion.factorialLength(10000) shouldBe 35660
  }

  "#maxSubsequence" should "return 6 for {1,2,3}" in {
    Recursion.maxSubsequence(List(1,2,3)) shouldBe 6
  }

  "#maxSubsequence" should "return 2 for {1,1,-1}" in {
    Recursion.maxSubsequence(List(1,1,-1)) shouldBe 2
  }

  "#maxSubsequence" should "return 214 for {-1,7,114,93,-15,3}" in {
    Recursion.maxSubsequence(List(-1,7,114,93,-15,3)) shouldBe 214
  }

  "#maxSubsequence" should "return 1 for {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}" in {
    Recursion.maxSubsequence(List(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1)) shouldBe 1
  }

  "#maxSubsequence" should "return 1234567890 for {1234567890,-123456789}" in {
    Recursion.maxSubsequence(List(1234567890,-123456789)) shouldBe 1234567890
  }
}
