package lectures.session4.part1_actor_model

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Part1_BasicActorModel extends App {
  class HelloActor extends Actor {
    override def receive: Receive = { case name: String =>
      println(s"Hello $name. You see this message from inside an actor.")
    }
  }

  val actorSystem = ActorSystem("basic-example")

  // Based on the actor model, we never have direct access to instances
  val actorReference = actorSystem.actorOf(Props[HelloActor](), "hello-actor")

  // Send a couple names to greet back
  actorReference.!("John")
  actorReference.!("Fabio")

  // Actors handle also messages that can't be parsed (see logs)
  actorReference.!(123L)

  Await.ready(actorSystem.terminate(), Duration.Inf)
}
