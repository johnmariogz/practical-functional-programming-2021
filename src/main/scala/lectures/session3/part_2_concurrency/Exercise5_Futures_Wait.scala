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
      n2 <- Future.sequence(List(f1, f2, f3))
    } yield n2.reduce(_ * _)

    val resultSequential = Await.result(multiplicationSequential, 330.millis)
    println(s"result sequential is $resultSequential")
  }

  Helpers.measureTime("parallel") {
    val value: List[Future[Int]] = List(
      f1,
      f2,
      f3
    )

    // List[Int] => Int
    val multiplicationParallel: Future[Int] = Future.sequence(value).map { list =>
      list.reduce(_ * _)
    }

    val resultParallel = Await.result(multiplicationParallel, 220.millis)
    println(s"result parallel is $resultParallel")
  }

  def sleep(delay: Long): Unit = Thread.sleep(delay)
}
