package lectures.session4.part_2_actor_model

import akka.actor._
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}

object Part2_ActorsInRealWorld extends App {
  implicit val timeout: Timeout = Timeout(1.second)

  case class StoreUrl(url: String)
  case class GetUrl(id: Int)

  case class StoredUrlResponse(id: Int, url: String)

  class CacheDBActor extends Actor {
    // Mutable hashmap - mutability is contained within a block
    // The data structure can be used here safely, even though is not thread-safe, WHY?
    private val urls = collection.mutable.HashMap[Int, String]()

    // TODO Count and print how many times storeUrl has been called, how to do it?
    override def receive: Receive = {
      case StoreUrl(url) =>
        val message = storeUrl(url)
        sender() ! message

      case GetUrl(id) =>
        val message = getUrl(id)
        sender() ! message
    }

    def storeUrl(url: String): StoredUrlResponse = {
      val id: Int = url.hashCode
      urls.put(id, url)
      StoredUrlResponse(id, url)
    }

    def getUrl(id: Int): Option[StoredUrlResponse] = {
      urls.get(id) match {
        case Some(url) =>
          Some(StoredUrlResponse(id, url))

        case None =>
          println(s"[warn] Id '$id' does not exist!")
          None
      }
    }
  }

  val actorSystem = ActorSystem("example")
  val actor       = actorSystem.actorOf(Props[CacheDBActor](), "cache-db-actor")

  val urls = List(
    "http://www.google.com",
    "http://www.code.berlin",
    "http://www.zalando.de",
    "http://www.berlin.de",
    "http://www.zalando.de"
  )

  // First step - store the Urls, all at the same time
  def sendStoreUrlMessage(url: String): Future[Any] =
    actor ? StoreUrl(url)

  val storeResult = Future.traverse(urls)(sendStoreUrlMessage(_))
  val messages    = Await.result(storeResult, 1.second)
  println(messages)

  // Second step
  val ids = Vector(
    1379822052, // www.berlin.de
    -565433797, // www.zalando.de
    1379822052, // www.berlin.de
    0           // No URL
  )

  def sendGetUrlMessage(id: Int): Future[Any] =
    actor ? GetUrl(id)

  // Parallel search
  val fetchResult = Future.traverse(ids)(sendGetUrlMessage)
  val storedUrls  = Await.result(fetchResult, 1.second)
  println(storedUrls)

  Await.ready(actorSystem.terminate(), Duration.Inf)
}
