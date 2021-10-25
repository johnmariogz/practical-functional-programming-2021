package lectures.session3.part_2_concurrency

import scala.collection.parallel.CollectionConverters._

object Exercise3_ProblemsWithParallelCollections extends App {
  val list = (1 to 20).toList.par

  // Side-effects are dangerous

  1.to(3).foreach { _ =>
    var sum = 0
    list.foreach(sum += _)
    println(s"sum = $sum")
  }

  // Printing is not in order
  list.foreach(println)

  // Non-Associative Operations also lead to non-determinism
  1.to(3).foreach { _ =>
    val reduced = list.reduce(_ - _)
    println(s"reduced = $reduced")
  }
}
