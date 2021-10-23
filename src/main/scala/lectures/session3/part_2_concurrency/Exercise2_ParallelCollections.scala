package lectures.session3.part_2_concurrency

import scala.collection.parallel.CollectionConverters._

object Exercise2_ParallelCollections extends App {
  val list = (1 to 10000).toList

  val sequentialReduce = list.reduce(_ + _)
  val parallelReduce   = list.par.reduce(_ + _)

  println(s"Sequential reduce = $sequentialReduce / parallel reduce = $parallelReduce")
}
