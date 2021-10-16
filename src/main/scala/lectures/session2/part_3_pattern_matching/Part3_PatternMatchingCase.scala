package lectures.session2.part_3_pattern_matching

object Part3_PatternMatchingCase extends App {
  sealed trait User
  case class NormalUser(id: String)                         extends User
  case class AdminUser(id: String, motto: String, age: Int) extends User

  // TODO Return the user's motto if possible, if not return "User is not powerful enough"
  def giveBackMotto(user: User): String = {
    user match {
      case _: NormalUser => "User is not powerful enough"
      case a: AdminUser  => a.motto
    }
  }

  def giveBackMotto2(user: User): String = {
    user match {
      case _: NormalUser          => "User is not powerful enough"
      case AdminUser(_, myVar, _) => myVar
    }
  }

  val normalUser: NormalUser  = NormalUser("i-am-a-normal-user")
  val normalUserMotto: String = giveBackMotto(normalUser)
  println(s"The normal user's motto is: $normalUserMotto")

  val adminUser: AdminUser   = AdminUser("i-am-powerful", "I have the power", 16)
  val adminUserMotto: String = giveBackMotto2(adminUser)
  println(s"The admin user's motto is: $adminUserMotto")
}
