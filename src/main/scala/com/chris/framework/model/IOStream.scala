package main.scala.com.chris.framework.model

import java.io.{InputStream, OutputStream}
import java.net.Socket

case class IOStream(var socket: Socket, var inputStream: InputStream, var outputStream: OutputStream)
