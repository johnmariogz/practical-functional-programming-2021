package exercises.session4

import akka.actor.{ActorSystem, Props}
import akka.pattern._
import akka.testkit.{ImplicitSender, TestKit}
import exercises.session4.Actors._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.{BeforeAndAfterAll, GivenWhenThen}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ActorsTest
    extends TestKit(ActorSystem("ActorsTest"))
    with AnyWordSpecLike
    with Matchers
    with ImplicitSender
    with BeforeAndAfterAll
    with ScalaFutures
    with GivenWhenThen {
  implicit val timeout = Actors.timeout

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "actors" should {
    "handle the reduction of inventory" in {
      val inventory  = system.actorOf(Props[InventoryActor](), "inventory")
      val baristaOne = system.actorOf(Props(classOf[BaristaActor], inventory), "barista-one")
      val baristaTwo = system.actorOf(Props(classOf[BaristaActor], inventory), "barista-two")
      val cashierOne =
        system.actorOf(Props(classOf[CashierActor], baristaOne, baristaTwo), "cashier-one")
      val cashierTwo =
        system.actorOf(Props(classOf[CashierActor], baristaOne, baristaTwo), "cashier-two")

      def makePurchase(purchase: Purchase): Future[Unit] = Future {
        val actor =
          if (System.nanoTime() % 2 == 0) // Naive attempt to do round-robin (VERY sub-optimal)
            cashierOne
          else
            cashierTwo

        actor ! purchase
      }

      Given("a café's phone backlog of pickup orders")
      val purchases = List(
        Purchase(Espresso, 1d),
        Purchase(Espresso, 2d),
        Purchase(Espresso, 3d),
        Purchase(LatteMacchiato, 1d),
        Purchase(LatteMacchiato, 0d),
        Purchase(HotChocolate, 0.5d),
        Purchase(Espresso, 2d),
        Purchase(HotChocolate, 2d),
        Purchase(HotChocolate, 0d),
        Purchase(LatteMacchiato, 0d)
      )

      When("orders are processed by multiple cashiers")
      Future.traverse(purchases)(makePurchase(_)).futureValue

      Then("only seven drinks can be prepared and three must be rejected")
      expectMsgAllClassOf(
        classOf[Container],
        classOf[Container],
        classOf[Container],
        classOf[Container],
        classOf[Container],
        classOf[Container],
        classOf[Container],
        classOf[RefundVoucher],
        classOf[RefundVoucher],
        classOf[RefundVoucher]
      )

      And("11.5 EUR should be present in tips between both cashiers")
      val totalTips = for {
        tipsCashierOne <- (cashierOne ? GetTips).mapTo[TotalTipsResponse]
        tipsCashierTwo <- (cashierTwo ? GetTips).mapTo[TotalTipsResponse]
      } yield TotalTipsResponse(tipsCashierOne.tips + tipsCashierTwo.tips)
      totalTips.futureValue shouldBe TotalTipsResponse(11.5d)
    }
  }
}
