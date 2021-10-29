package lectures.session3.part_2_concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Exercise4_Futures_Basic extends App {
  val f1 = Future {
    sleep(800)
    1
  }

  val f2 = Future {
    sleep(200)
    2
  }

  val f3 = Future {
    sleep(400)
    3
  }

  val result = for {
    r1 <- f1
    r2 <- f2
    r3 <- f3
  } yield r1 + r2 + r3

  println("Before waiting = " + result)
  sleep(2000)
  println("After waiting = " + result)

  def sleep(delay: Long): Unit = Thread.sleep(delay)
}
