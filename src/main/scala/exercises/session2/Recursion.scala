package exercises.session2

import scala.annotation.tailrec

object Recursion {
  // Function that calculates the factorial and returns the LENGTH of the factorial
  // E.g. 5! = 120 => factorialLength(5) = 3
  // Parameter n can be [1, 10.000]
  def factorialLength(n: Int): Int = {
    @tailrec
    def factorial(n: Int, total: BigInt = 1): BigInt = n match {
      case 0 => total
      case _ => factorial(n - 1, total * n)
    }

    @tailrec
    def totalLength(n: BigInt, base: Int = 10, count: Int = 0): Int = {
      if (n == 0) count
      else totalLength(n / base, base, count + 1)
    }

    val result = factorial(n)
    totalLength(result)
  }

  // Function that returns the maximum continuous subsequence of numbers
  // The function should go over the list only once.
  // E.g. [1, 2, 3] => 6
  def maxSubsequence(list: List[Int]): Int = {
    @tailrec
    def findMaxSubArray(l: List[Int], max: Int, acc: Int): Int = l match {
      case Nil =>
        max

      case head :: tail =>
        val newAcc = math.max(head, head + acc)
        val newMax = math.max(max, newAcc)
        findMaxSubArray(tail, newMax, newAcc)
    }

    val max = list.take(1).sum

    findMaxSubArray(list.drop(1), max, max)
  }
}
