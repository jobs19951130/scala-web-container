package com.chris.framework.util

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class ActorPool extends Actor{
  // 重写接受消息的偏函数，其功能是接受消息并处理
  override def receive: Receive = {
    case IOStream(socket,inputStream,outputStream) =>
      val request = new WarpRequest(inputStream)
      val response = new Response(outputStream)
      val requestInfo = request.parseRequest()
      if(requestInfo!=null){
        DoAction.process(requestInfo,response)
      }
      outputStream.close()
      request.close
      socket.close()
    case "丑八怪" => println("滚犊子 ！")
    case "stop" => {
      context.stop(self) // 停止自己的actorRef
      context.system.terminate() // 关闭ActorSystem，即关闭其内部的线程池（ExcutorService）
    }
  }
}

object ActorPool {
  private val MyFactory = ActorSystem("myFactory")    //里面的"myFactory"参数为线程池的名称
  def getActor(actorName:String):ActorRef={
    MyFactory.actorOf(Props[ActorPool], actorName)

  }
}