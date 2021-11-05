package lectures.session4.part_1_functional_state

object Part1_AlteringState extends App {
  def swap(elements: List[Int]): List[Int] = {
    val array = elements.toArray
    if (array.size == 2) {
      val tmp = array(0)
      array(0) = array(1)
      array(1) = tmp
      array.toList
    } else {
      elements
    }
  }

  val listOne = List(2, 4)
  val swapped = swap(listOne)
  println(s"Before swap = $listOne | Swapped elements = $swapped")

  val listTwo    = List(10, 3, 4, 5, 6)
  val notSwapped = swap(listTwo)
  println(s"Before swap = $listTwo | Not swapped elements = $notSwapped")
}
