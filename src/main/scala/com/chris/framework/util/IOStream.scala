package com.chris.framework.util

import java.io.{InputStream, OutputStream}
import java.net.Socket

case class IOStream(var socket: Socket, var inputStream: InputStream, var outputStream: OutputStream)
