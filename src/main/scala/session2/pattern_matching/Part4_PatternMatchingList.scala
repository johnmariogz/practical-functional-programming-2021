package berlin.code.functional
package session2.pattern_matching

object Part4_PatternMatchingList extends App {
  //  listOne = 1 :: (2  :: Nil)
  //                  2 (:: Nil)
  //                        Nil
  val listOne: List[Int] = List(1, 2)

  // TODO for empty lists => "This list is empty!"
  //  TODO for the rest "Oh look! I found $x on the first position AND $y elements are left"
  def giveListHeadAndTailSize(list: List[Int]): String = {
    ???
  }

  val listOneHeadAndTail = giveListHeadAndTailSize(listOne)
  println(s"For list one we got: $listOneHeadAndTail")

  val listTwo: List[Int]   = ??? // TODO How to make it work for empty lists?
  val emptyListHeadAndTail = giveListHeadAndTailSize(listTwo)
  println(s"For list two we got: $emptyListHeadAndTail")
}
