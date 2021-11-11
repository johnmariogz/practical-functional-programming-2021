package lectures.session4.part_2_actor_model

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.StdIn

object Part1_BasicActorModel extends App {
  case class Message(message: String)
  case object Finish

  class HelloActor extends Actor {
    override def receive: Receive = {
      case Message(message) =>
        println(s"You said '$message'. '$message' to you as well!")

      case Finish =>
        println("That's all folks!")
    }
  }

  val actorSystem = ActorSystem("basic-example")

  // Based on the actor model, we never have direct access to instances
  val actorReference = actorSystem.actorOf(Props[HelloActor](), "hello-actor")

  @tailrec
  def execute(actorRef: ActorRef): Unit = {
    println("Type something (q to quit): ")
    val input = StdIn.readLine()
    if (input == "q") {
      actorReference ! Finish
    } else {
      actorReference ! Message(input)
      execute(actorRef)
    }
  }

  execute(actorReference)

  Await.ready(actorSystem.terminate(), Duration.Inf)
}
