package lectures.session4.part_1_functional_state

object Part2_FunctionalState extends App {
  import Helpers._

  // Same example (swap two numbers) but functionally

  val p = new RunnableST[(Int, Int)] {
    override def apply[S]: ST[S, (Int, Int)] = ???
    // Create references
    // Read them
    // Swap them
    // Read them again
  }

  println(ST.runST(p))
}
