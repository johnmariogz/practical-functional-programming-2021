package lectures.session4.part_1_functional_state

object Part2_FunctionalState extends App {
  import Helpers._

  // Same example (swap two numbers) but functionally

  val p = new RunnableST[(Int, Int)] {
    // 3 and 7
    override def apply[S]: ST[S, (Int, Int)] = for {
      // Create references
      r1 <- STRef(3)
      r2 <- STRef(7)
      // Read them
      a <- r1.read
      b <- r2.read
      // Swap them
      _ <- r1.write(b)
      _ <- r2.write(a)
      // Read them again
      x <- r1.read
      y <- r2.read
    } yield (x, y)
  }

  println(ST.runST(p))
}
