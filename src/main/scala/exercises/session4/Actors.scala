package exercises.session4

import akka.actor.{Actor, ActorRef}
import akka.pattern._
import akka.util.Timeout

import scala.collection.mutable
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

    private var tips = 0d

    override def receive: Receive = {
      case Purchase(drink, tip) =>
        tips += tip
        // Pass the drink orders one of the baristas
        barista ! Order(drink, sender())

      case GetTips =>
        sender() ! TotalTipsResponse(tips)
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
      val preparationResult: Future[PurchasedOrder] = for {
        storage <- (inventory ? GetFromStorage(drink.storageIngredient)).mapTo[StorageResult]
        fridge  <- (inventory ? GetFromFridge(drink.fridgeIngredient)).mapTo[FridgeResult]
      } yield prepareOrder(drink, storage, fridge)

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
    private val fridge: mutable.Map[FridgeIngredient, Int] =
      scala.collection.mutable.HashMap[FridgeIngredient, Int](
        Milk -> 4
      )

    private val storage: mutable.Map[StorageIngredient, Int] =
      scala.collection.mutable.HashMap[StorageIngredient, Int](
        Coffee -> 6,
        Cocoa  -> 2
      )

    override def receive: Receive = {
      case GetFromFridge(fridgeIngredient) =>
        sender() ! getAndReduceFromFridge(fridgeIngredient)

      case GetFromStorage(storageIngredient) =>
        sender() ! getAndReduceFromStorage(storageIngredient)
    }

    def getAndReduceFromStorage(ingredient: StorageIngredient): StorageResult = {
      storage.get(ingredient) match {
        case Some(amount) if amount > 0 =>
          storage.update(ingredient, amount - 1)
          StorageIngredientUsed(ingredient)

        case Some(_) | None =>
          StorageOutOfStock(ingredient)
      }
    }

    def getAndReduceFromFridge(maybeIngredient: Option[FridgeIngredient]): FridgeResult = {
      // If there's enough of the ingredient: return FridgeIngredientUsed
      // If not enough: return FridgeOutOfStock
      // If the ingredient is not needed: return FridgeIngredientNotNeeded
      maybeIngredient match {
        case Some(ingredient) =>
          fridge.get(ingredient) match {
            case Some(amount) if amount > 0 =>
              fridge.update(ingredient, amount - 1)
              FridgeIngredientUsed(ingredient)

            case Some(_) | None =>
              FridgeOutOfStock(ingredient)
          }

        case None =>
          FridgeIngredientNotNeeded
      }
    }
  }
}
