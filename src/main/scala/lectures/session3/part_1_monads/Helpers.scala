package lectures.session3.part_1_monads

object Helpers {
  def readInput(hint: String): String = {
    println(s"\nEnter an input (hint: $hint) >")
    val s = Console.in.readLine()
    println()
    s
  }
}
