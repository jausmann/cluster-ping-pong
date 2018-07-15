package controllers

import akka.actor.ActorRef

case class Count(sender: ActorRef, count: Long)
