package lectures.session3.part_2_concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Exercise5_Futures_Wait extends App {
  def f1: Future[Int] = Future {
    sleep(100)
    50
  }

  def f2: Future[Int] = Future {
    sleep(200)
    10
  }

  def f3: Future[Int] = Future {
    30
  }

  Helpers.measureTime("sequential") {
    val multiplicationSequential = for {
      n1 <- f1
      n2 <- f2
      n3 <- f3
    } yield n1 * n2 * n3

    val resultSequential = Await.result(multiplicationSequential, 330.millis)
    println(s"result sequential is $resultSequential")
  }

  Helpers.measureTime("parallel") {
    val multiplicationParallel = ??? // TODO [parallel] calculate the multiplications

    val resultParallel = Await.result(multiplicationParallel, 220.millis)
    println(s"result parallel is $resultParallel")
  }

  def sleep(delay: Long): Unit = Thread.sleep(delay)
}
