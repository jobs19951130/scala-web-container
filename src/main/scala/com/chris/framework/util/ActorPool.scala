package com.chris.framework.util

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import main.scala.com.chris.framework.actions.DoAction
import main.scala.com.chris.framework.model.{IOStream, Response, WarpRequest}

class ActorPool extends Actor{
  override def receive: Receive = {
    case IOStream(socket,inputStream,outputStream) =>
      val request = new WarpRequest(inputStream)
      val response = new Response(outputStream)
      val requestInfo = request.parseRequest()
      if(requestInfo!=null){
        DoAction.response(requestInfo,response)
      }
      outputStream.close()
      request.close
      socket.close()
    case "stop" => {
      context.stop(self)
      context.system.terminate()
    }
  }
}

object ActorPool {
  private val MyFactory = ActorSystem("myFactory")
  def getActor(actorName:String):ActorRef={
    MyFactory.actorOf(Props[ActorPool], actorName)

  }
}