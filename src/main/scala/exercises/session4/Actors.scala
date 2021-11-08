package exercises.session4

import akka.actor.{Actor, ActorRef}
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object Actors {
  implicit val timeout: Timeout = Timeout(1.second)

  sealed trait Ingredient
  sealed trait FridgeIngredient  extends Ingredient
  sealed trait StorageIngredient extends Ingredient

  case object Milk   extends FridgeIngredient
  case object Coffee extends StorageIngredient
  case object Cocoa  extends StorageIngredient

  sealed trait Drink {
    val storageIngredient: StorageIngredient
    val fridgeIngredient: Option[FridgeIngredient]
  }

  case object LatteMacchiato extends Drink {
    override val storageIngredient: StorageIngredient       = Coffee
    override val fridgeIngredient: Option[FridgeIngredient] = Some(Milk)
  }

  case object Espresso extends Drink {
    override val storageIngredient: StorageIngredient       = Coffee
    override val fridgeIngredient: Option[FridgeIngredient] = None
  }

  case object HotChocolate extends Drink {
    override val storageIngredient: StorageIngredient       = Cocoa
    override val fridgeIngredient: Option[FridgeIngredient] = Some(Milk)
  }

  case class Purchase(drink: Drink, tip: Double)
  case object GetTips
  case class TotalTipsResponse(tips: Double)

  class CashierActor(baristaOne: ActorRef, baristaTwo: ActorRef) extends Actor {
    private val baristas = scala.collection.mutable.Queue[ActorRef](
      baristaOne,
      baristaTwo
    )

    // Balance the baristas so that they handle orders in an alternate manner
    private def barista: ActorRef = {
      val actor = baristas.dequeue()
      baristas.enqueue(actor)
      actor
    }

    override def receive: Receive = {
      case Purchase(drink, _) =>
        // TODO Store the tips (coming within Purchase)

        // Pass the drink orders one of the baristas
        barista ! Order(drink, ???) // TODO: Pass the customer (the sender())

      case GetTips =>
      // TODO: Send back the message informing how much has been left in tips
    }
  }

  case class Order(drink: Drink, customer: ActorRef)

  sealed trait PurchasedOrder
  case class RefundVoucher(drink: Drink) extends PurchasedOrder
  case class Container(drink: Drink)     extends PurchasedOrder

  class BaristaActor(inventory: ActorRef) extends Actor {
    private def prepareOrder(
        drink: Drink,
        storage: StorageResult,
        fridge: FridgeResult
    ): PurchasedOrder =
      (storage, fridge) match {
        case (StorageIngredientUsed(_), FridgeIngredientUsed(_)) |
            (StorageIngredientUsed(_), FridgeIngredientNotNeeded) =>
          Container(drink)

        case (_, _) =>
          RefundVoucher(drink)
      }

    override def receive: Receive = { case Order(drink, customer) =>
      // TODO: Call the inventory to see what is available and get it out
      val preparationResult: Future[PurchasedOrder] = for {
        storage <- (inventory ? GetFromStorage(drink.storageIngredient)).mapTo[StorageResult]
        // TODO: Call the inventory for the fridge ingredients
      } yield prepareOrder(drink, storage, ???)

      preparationResult.pipeTo(customer)
    }
  }

  case class GetFromFridge(fridgeIngredient: Option[FridgeIngredient])
  case class GetFromStorage(storageIngredient: StorageIngredient)

  sealed trait StorageResult
  case class StorageOutOfStock(ingredient: StorageIngredient)     extends StorageResult
  case class StorageIngredientUsed(ingredient: StorageIngredient) extends StorageResult

  sealed trait FridgeResult
  case object FridgeIngredientNotNeeded                         extends FridgeResult
  case class FridgeOutOfStock(ingredient: FridgeIngredient)     extends FridgeResult
  case class FridgeIngredientUsed(ingredient: FridgeIngredient) extends FridgeResult

  class InventoryActor extends Actor {
    val fridge = scala.collection.mutable.HashMap[FridgeIngredient, Int](
      Milk -> 4
    )

    val storage = scala.collection.mutable.HashMap[StorageIngredient, Int](
      Coffee -> 6,
      Cocoa  -> 2
    )

    override def receive: Receive = {
      case _: GetFromFridge =>
      // TODO: Call getAndReduceFromFridge() and send back to whoever called (use sender() to find the caller)

      case _: GetFromStorage =>
      // TODO: Call getAndReduceFromStorage() and send back result to whoever called (use sender() to find the caller)
    }

    // TODO: Reduce from the storage stock IFF the ingredient's quantity is positive
    def getAndReduceFromStorage(ingredient: StorageIngredient): StorageResult = {
      // If there's enough of the ingredient: return StorageIngredientUsed
      // If not enough: return StorageOutOfStock
      ???
    }

    // TODO: Reduce from the fridge stock IFF the ingredient's quantity is positive
    def getAndReduceFromFridge(ingredient: Option[FridgeIngredient]): FridgeResult = {
      // If there's enough of the ingredient: return FridgeIngredientUsed
      // If not enough: return FridgeOutOfStock
      // If the ingredient is not needed: return FridgeIngredientNotNeeded
      ???
    }
  }
}
