package lectures.session4.part_1_functional_state

object Part2_FunctionalState extends App {
  import Helpers._

  // Same example (swap two numbers) but functionally
  val p = new RunnableST[(Int, Int)] {
    override def apply[S]: ST[S, (Int, Int)] = for {
      r1 <- STRef(2)
      r2 <- STRef(4)
      x  <- r1.read
      y  <- r2.read
      _  <- r1.write(y)
      _  <- r2.write(x)
      a  <- r1.read
      b  <- r2.read
    } yield (a, b)
  }

  println(ST.runST(p))
}
