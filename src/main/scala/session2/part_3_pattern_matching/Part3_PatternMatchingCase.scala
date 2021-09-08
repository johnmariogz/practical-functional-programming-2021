package berlin.code.functional
package session2.part_3_pattern_matching

object Part3_PatternMatchingCase extends App {
  sealed trait User
  case class NormalUser(id: String)               extends User
  case class AdminUser(id: String, motto: String) extends User

  // TODO Return the user's motto if possible, if not return "User is not powerful enough"
  def giveBackMotto(user: User): String = {
    user match {
      case _: NormalUser => ???
      case _: AdminUser  => ???
    }
  }

  val normalUser: NormalUser  = NormalUser("i-am-a-normal-user")
  val normalUserMotto: String = giveBackMotto(normalUser)
  println(s"The normal user's motto is: $normalUserMotto")

  val adminUser: AdminUser   = AdminUser("i-am-powerful", "I have the power")
  val adminUserMotto: String = giveBackMotto(adminUser)
  println(s"The admin user's motto is: $adminUserMotto")
}
