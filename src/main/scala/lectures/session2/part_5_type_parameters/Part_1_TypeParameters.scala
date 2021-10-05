package lectures.session2.part_5_type_parameters

object Part_1_TypeParameters extends App {
  sealed trait Container[A] {
    def value: A
  }

  case class IntContainer(value: Int)       extends Container[Int]
  case class StringContainer(value: String) extends Container[String]

  val containerInt = IntContainer(42)
  println(s"containerInt = $containerInt")

  val containerString = StringContainer("foobar")
  println(s"containerString = $containerString")

}
