package main.scala.com.chris.framework.model

import java.io.{BufferedWriter, IOException, OutputStream, OutputStreamWriter}
import java.net.Socket
import java.util.Date

class Response{
  var len:Int = 0
  var content:StringBuilder = new StringBuilder
  var header:StringBuilder = new StringBuilder
  val CRLF:String = "\r\n"
  var BLANK:String = " "
  var  bw:BufferedWriter = null
  var outputStream:OutputStream = null
  def this(client:Socket){
    this()
    try {bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream))
    }
    catch {
      case ex: IOException=>
        header=null
    }

  }
  def this(os:OutputStream){
    this()
    this.outputStream = os
    bw = new BufferedWriter(new OutputStreamWriter(os))
  }
  def print(info:String):Response={
    content.append(info)
    len += info.getBytes.length
    this
  }
  def println(info:String): Response ={
    content.append(info).append(CRLF)
    len+=(info+CRLF).getBytes.length
    this
  }
  def createHeader(code:Int,contentType:String)={
    header.append("HTTP/1.1").append(BLANK).append(code).append(BLANK)
    code match {
      case 200=>
        header.append("OK")
      case 404=>
        header.append("NOT FOUND")
      case 505=>
        header.append("SERVER ERROR")
    }
    header.append(CRLF)
    header.append("Server:bjsxt Server/0.0.1").append(CRLF)
    header.append("Date:").append(new Date).append(CRLF)//  Content-Type:image/jped
    header.append(contentType).append(CRLF)
    //正文长度 ：字节长度
    header.append("Content-Length:").append(len).append(CRLF)
    header.append(CRLF) //分隔符
  }
  def push2Client(code:Int,contentType:String)={
    createHeader(code,contentType)
    bw.append(header.toString)
    bw.append(content.toString)
    bw.flush
  }

  def closeIO()={
    outputStream.close()
    bw.close()
  }
}
