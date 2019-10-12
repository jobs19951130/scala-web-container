package com.chris.framework.main

import java.net.ServerSocket

import com.chris.framework.configuration.Config
import com.chris.framework.util.{ActorPool, IOStream}

object MyFrameworkApp {

  def startServer()={
    val server = new ServerSocket(Config().port)
    val actor = ActorPool.getActor("actor")
    while(true){
      val socket = server.accept()
      actor!IOStream(socket,socket.getInputStream,socket.getOutputStream)
    }
  }

  def main(args: Array[String]): Unit = {
    startServer()
  }
}
