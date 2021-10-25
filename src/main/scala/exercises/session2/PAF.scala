package exercises.session2

object PAF {
  def map[A, B](l: List[A])(f: A => B): List[B] = l.map(f)

  def filter[A](l: List[A])(f: A => Boolean): List[A] = l.filter(f)
}
