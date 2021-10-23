package lectures.session3.part_2_concurrency

import java.util.concurrent.TimeUnit

object Helpers {
  def measureTime(name: String)(f: => Any): Unit = {
    val t0 = System.nanoTime()
    f
    val t1    = System.nanoTime()
    val total = TimeUnit.NANOSECONDS.toMillis(t1 - t0)
    println(s"Execution of $name took $total ms")
  }
}
