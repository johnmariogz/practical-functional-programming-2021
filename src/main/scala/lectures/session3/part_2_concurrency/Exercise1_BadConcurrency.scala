package lectures.session3.part_2_concurrency

object Exercise1_BadConcurrency extends App {
  // TODO What happens if it's immutable?
  class Subject(
      var name: String,
      var student: String,
      var university: String
  ) {
    override def toString: String = s"name: $name, student: $student, university: $university"
  }

  val course = new Subject("Theoretical Functional Programming", "John", "TU Berlin")

  val t1 = new Thread {
    override def run(): Unit = {
      Thread.sleep(1000)
      course.name = "Practical Functional Programming"
      Thread.sleep(3000)
      course.university = "Code University"
    }
  }

  // Start the thread
  t1.start()

  println(s"[1] $course")

  Thread.sleep(2000)
  println(s"[2] $course")

  Thread.sleep(2000)
  println(s"[3] $course")

  t1.join()
}
