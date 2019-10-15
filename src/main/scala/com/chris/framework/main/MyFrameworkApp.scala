package com.chris.framework.main

import java.net.ServerSocket

import com.chris.framework.configuration.Config
import com.chris.framework.util.ActorPool
import main.scala.com.chris.framework.model.IOStream

object MyFrameworkApp {

  def startServer()={
    val server = new ServerSocket(Config().item("port").toInt)
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
