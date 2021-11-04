package lectures.session4.part1_actor_model

import akka.actor._
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}

object Part2_ActorsWithUsage extends App {
  implicit val timeout: Timeout = Timeout(1.second)

  case class StoreUrl(url: String)
  case class StoredUrlResponse(id: Int, url: String)
  case class GetUrl(id: Int)

  class DatabaseActor extends Actor {
    // Mutable hashmap - mutability is contained within a block
    // The data structure can be used here safely, even though is not thread-safe, WHY?
    val urls = collection.mutable.HashMap[Int, String]()

    // TODO Count and print how many times storeUrl has been called, how to do it?
    override def receive: Receive = {
      case StoreUrl(url) =>
        val result = storeUrl(url)
        sender() ! result

      case GetUrl(id) =>
        val result = getUrl(id)
        sender() ! result
    }

    private def storeUrl(url: String): StoredUrlResponse = {
      val urlId = url.##
      println(s"[info] Trying to store '$url'")
      if (urls.get(urlId).isEmpty) {
        urls.put(urlId, url)
      } else {
        println(s"[warn] '$url' is already present!")
      }

      StoredUrlResponse(urlId, url)
    }

    private def getUrl(id: Int): Option[StoredUrlResponse] = {
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
  val actor       = actorSystem.actorOf(Props[DatabaseActor](), "handler-actor")

  val urls = List(
    "http://www.google.com",
    "http://www.code.berlin",
    "http://www.zalando.de",
    "http://www.berlin.de",
    "http://www.zalando.de"
  )

  // First step - store the Urls, all at the same time
  val storeResult = Future.traverse(urls)(url => actor ? StoreUrl(url))
  val messages    = Await.result(storeResult, 1.second)
  println(messages)

  // Second step
  val ids = List(
    1379822052, // www.berlin.de
    -565433797, // www.zalando.de
    1379822052, // www.berlin.de
    0           // No URL
  )

  val fetchResult = Future.traverse(ids)(id => actor ? GetUrl(id))
  val storedUrls  = Await.result(fetchResult, 1.second)
  println(storedUrls)

  Await.ready(actorSystem.terminate(), Duration.Inf)
}
