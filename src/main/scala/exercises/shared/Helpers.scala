package exercises.shared

import java.util.concurrent.TimeUnit
import scala.concurrent.{ExecutionContext, Future}

object Helpers {
  def measureTime(name: String)(f: => Any): Unit = {
    println(s"Executing '$name'")
    val t0 = System.nanoTime()
    f
    val t1    = System.nanoTime()
    val total = TimeUnit.NANOSECONDS.toMillis(t1 - t0)
    println(s"Execution of '$name' took $total ms")
  }

  def measureTimeF[T](name: String)(f: => Future[T])(implicit ec: ExecutionContext): Future[T] = {
    println(s"Executing '$name'")
    val t0 = System.nanoTime()
    f.andThen { e =>
      val t1    = System.nanoTime()
      val total = TimeUnit.NANOSECONDS.toMillis(t1 - t0)
      println(s"Execution of '$name' took $total ms [execution = $e]")
    }
  }
}
