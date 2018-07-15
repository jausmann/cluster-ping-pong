package controllers

import akka.actor.Actor
import play.api.Logger
import scala.concurrent.duration._

class PingPongActor(private val prefix: String,
                    private val logger: Logger) extends Actor {

  override def receive: Actor.Receive = {
    case Count(sender, count) =>
      logger.info(s"$prefix $count")

      import scala.concurrent.ExecutionContext.Implicits.global

      context.system.scheduler.scheduleOnce(1 seconds) {
        sender ! Count(self, count + 1)
      }
  }
}
