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

  case class TotalTips(tips: Double)

  class CashierActor(baristaOne: ActorRef, baristaTwo: ActorRef) extends Actor {
    // TODO: Balance the baristas so that they handle orders in an alternate manner
    private var tips = 0d
    private val baristas = scala.collection.mutable.Queue[ActorRef](
      baristaOne,
      baristaTwo
    )

    private def barista: ActorRef = {
      val actor = baristas.dequeue()
      baristas.enqueue(actor)
      actor
    }

    override def receive: Receive = {
      case Purchase(drink, tip) =>
        // TODO Store the tips
        tips += tip
        // TODO Pass the drink orders to the barista
        barista ! Order(drink, sender())

      case GetTips =>
        // TODO: Send back the message informing how much has been left in tips
        sender() ! TotalTips(tips)
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
        fridge  <- (inventory ? GetFromFridge(drink.fridgeIngredient)).mapTo[FridgeResult]
      } yield prepareOrder(drink, storage, fridge)

      // TODO: Send back to the customer, use akka.pattern._ and Future(...).pipeTo(actor)
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
    private val fridge = scala.collection.mutable.HashMap[FridgeIngredient, Int](
      Milk -> 4
    )

    private val storage = scala.collection.mutable.HashMap[StorageIngredient, Int](
      Coffee -> 6,
      Cocoa  -> 2
    )

    override def receive: Receive = {
      case GetFromFridge(ingredient) =>
        // TODO Reduce items from the fridge (if present) and send back to whoever called
        val answer = getAndReduceFromFridge(ingredient)
        sender() ! answer

      case GetFromStorage(ingredient) =>
        // TODO Reduce items from the storage (if present) and send back to whoever called
        val answer = getAndReduceFromStorage(ingredient)
        sender() ! answer
    }

    private def getAndReduceFromStorage(ingredient: StorageIngredient): StorageResult = {
      storage.get(ingredient) match {
        case Some(left) if left > 0 =>
          storage.put(ingredient, left - 1)
          StorageIngredientUsed(ingredient)

        case Some(_) | None =>
          StorageOutOfStock(ingredient)
      }
    }

    private def getAndReduceFromFridge(ingredient: Option[FridgeIngredient]): FridgeResult =
      ingredient match {
        case Some(ingredient) =>
          fridge.get(ingredient) match {
            case Some(left) if left > 0 =>
              fridge.put(ingredient, left - 1)
              FridgeIngredientUsed(ingredient)

            case Some(_) | None =>
              FridgeOutOfStock(ingredient)
          }

        case None =>
          FridgeIngredientNotNeeded
      }
  }

}
