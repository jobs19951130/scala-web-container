package com.chris.framework.util
import akka.pattern.ask
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class ActorPool() extends Actor{
  override def receive: Receive = {
    case "act1" => sender()!"received act1"
    case "act2" => println("act2")
    case "shutdown" => {
      context.stop(self)
      context.system.terminate()
    }
  }
}
object ActorPool {

  private var threadNum = 0
  def getActorPool(num:Int):List[ActorRef]={
    this.threadNum = num
    for(i <- 0 to threadNum){
      threads = threads:+actorFactory.actorOf(Props[ActorPool],"frameworkActor"+i)

    }
    this.threads
  }
  private val actorFactory = ActorSystem ("actorFactory")
  private var threads : List[ActorRef] = List[ActorRef]()
}
