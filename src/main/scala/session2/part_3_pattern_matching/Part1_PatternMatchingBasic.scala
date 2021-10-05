package berlin.code.functional
package session2.part_3_pattern_matching

object Part1_PatternMatchingBasic extends App {
  // TODO Print "We have a Bar" or "A Bizz just came in"
  def giveBackGreeting(name: String): String = {
    ???
  }

  val bar                 = "Bar"
  val greetingBar: String = giveBackGreeting(bar)
  println(s"The greeting for Bar is: $greetingBar")

  val bizz                 = "Bizz"
  val greetingBizz: String = giveBackGreeting(bizz)
  println(s"The greeting for Bizz is: $greetingBizz")
}
