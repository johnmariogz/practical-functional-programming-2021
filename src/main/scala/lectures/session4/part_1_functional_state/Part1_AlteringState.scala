package lectures.session4.part_1_functional_state

object Part1_AlteringState extends App {
  // Swap elements of a list IF it's of size 2 WITH mutation
  def swap(elements: List[Int]): List[Int] = {
    ???
  }

  val listOne = List(2, 4)
  val swapped = swap(listOne)
  println(s"Before swap = $listOne | Swapped elements = $swapped")

  val listTwo    = List(10, 3, 4, 5, 6)
  val notSwapped = swap(listTwo)
  println(s"Before swap = $listTwo | Not swapped elements = $notSwapped")
}
