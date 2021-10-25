package lectures.session2.part_3_pattern_matching

object Part2_PatternMatchingSealed extends App {
  sealed trait Foo
  class Bar  extends Foo
  class Bizz extends Foo
  class Bezz extends Foo

  // TODO Print "We have a Bar" or "A Bizz just came in"
  def giveBackGreeting(foo: Foo): String = {
    foo match {
      case _: Bar  => "We have a Bar"
      case _: Bizz => "A Bizz just came in"
      case _: Bezz => "It's indeed Bezz"
    }
  }

  val bar: Bar            = new Bar
  val greetingBar: String = giveBackGreeting(bar)
  println(s"The greeting for Bar is: $greetingBar")

  val bizz: Bizz           = new Bizz
  val greetingBizz: String = giveBackGreeting(bizz)
  println(s"The greeting for Bizz is: $greetingBizz")
}
