package controllers

import akka.actor.{ActorSystem, Address, Props}
import akka.cluster.Cluster
import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Logger}
import play.api.mvc.{InjectedController, Results}

@Singleton
class PingPongController @Inject()(actorSystem: ActorSystem,
                                   configuration: Configuration) extends InjectedController {

  val logger: Logger = Logger("application")

  val prefix = configuration.get[String]("prefix")
  val port = if (prefix == "ping") 2552 else 2553

  val extension = Cluster(actorSystem)
  val address = Address(protocol = "akka.tcp", system = "application", host = "127.0.0.1", port = port)
  extension.join(address)

  val actor = actorSystem.actorOf(Props(new PingPongActor(prefix, logger)), prefix)

  if (prefix == "pong") {
    actorSystem.actorSelection(s"akka.tcp://application@127.0.0.1:2552/user/ping") ! Count(actor, 0)
  }

  def start() = Action { implicit request =>
    Results.Ok("Ok")
  }
}
