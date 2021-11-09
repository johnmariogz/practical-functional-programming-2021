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
    override def receive: Receive = ???
  }

  val actorSystem = ActorSystem("basic-example")

  // Based on the actor model, we never have direct access to instances
  val actorReference = actorSystem.actorOf(Props[HelloActor](), "hello-actor")

  @tailrec
  def execute(actorRef: ActorRef): Unit = {
    println("Type something (q to quit): ")
    val input = StdIn.readLine()
    if (input == "q")
      ??? // TODO: Send Finish to the actor
    else {
      //  TODO: Send Message to the actor
      execute(actorRef)
    }
  }

  execute(actorReference)
  // TODO What happens if we send a message that is not handled?

  Await.ready(actorSystem.terminate(), Duration.Inf)
}
