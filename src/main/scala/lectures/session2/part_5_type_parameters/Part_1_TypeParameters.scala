package lectures.session2.part_5_type_parameters

object Part_1_TypeParameters extends App {
  sealed trait Container[MyValue] {
    def value: MyValue
  }

  case class IntContainer(value: Int)       extends Container[Int]
  case class StringContainer(value: String) extends Container[String]

  val containerInt = IntContainer(42)
  println(s"containerInt = $containerInt - ${foo(containerInt)}")

  val containerString = StringContainer("foobar")
  println(s"containerString = $containerString - ${foo(containerString)}")


  def foo(container: Container[_]): String = {
    container match {
      case IntContainer(value) => s"${value * value}"
      case StringContainer(value) => s"$value - $value"
    }
  }
}
