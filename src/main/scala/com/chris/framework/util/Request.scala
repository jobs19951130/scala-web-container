package com.chris.framework.util

import java.io.InputStream
import java.net.URLDecoder.decode
import java.util.StringTokenizer

case class Request(var method:String,var url:String,var parameter:Map[String,String],var header:Map[String,String],var body:Map[String,String]){}

class WarpRequest(inputStream: InputStream){

  val CRLF = "\r\n"
  var requestInfo:String=""
  var url:String=""
  var parameter:Map[String,String]=Map()
  var header:Map[String,String]=Map()
  var body:Map[String,String]=Map()
  def parseRequest(): Request ={

    var method:String=""
    val data = new Array[Byte](20480)
    var requestParameter:String = ""
    val len:Int = inputStream.read(data)
    if(len==(-1)){
      return null
    }
    requestInfo = new String(data,0,len)
    val firstLine:String = requestInfo.substring(0,requestInfo.indexOf(CRLF))
    val index:Int = requestInfo.indexOf("/")
    method = firstLine.substring(0,index).trim
    val urlStr = firstLine.substring(index, firstLine.indexOf("HTTP/")).trim

    val headerStr = requestInfo.substring(requestInfo.indexOf(CRLF),requestInfo.indexOf("\r\n\r\n")).trim
    header = parseParameter(headerStr,"header")
    val bodyStr = requestInfo.substring(requestInfo.indexOf("\r\n\r\n")).trim


    if (method.equalsIgnoreCase("post")) {
      url = urlStr
      body = parseParameter(bodyStr,"body")
    }

    else if (method.equalsIgnoreCase("get")) if (urlStr.contains("?")) { //是否存在参数
      val urlArray = urlStr.split("\\?")
      url = urlArray(0)
      requestParameter = urlArray(1) //接收请求参数
      parameter = parseParameter(requestParameter,"parameter")
    }

    else url = urlStr
    Request(method,url,parameter,header,body)
  }
  def parseParameter(requestParameter : String, method : String):Map[String,String]={
    //println(method+"------"+requestParameter)
    var split = Tuple2("","")
    var tempRequestParameter = requestParameter
    split=method match {
      case "parameter" =>
        Tuple2("&","=")
      case "header" =>
        Tuple2(CRLF,":")
      case "body" =>
        tempRequestParameter = requestParameter.substring(1,requestParameter.length-1)
        Tuple2(",",":")

    }
    var parameterMapValues:Map[String, String] = Map()
    val token = new StringTokenizer(tempRequestParameter, split._1)
    while ( {
      token.hasMoreTokens
    }) {
      val keyValue = token.nextToken
      var keyValues = keyValue.split(split._2)
      if (keyValues.length == 1) {
        val temp = keyValues
        keyValues = new Array[String](keyValues.length + 1)
        keyValues(0) = temp(0)
        keyValues(1) = null
      }
      val key = keyValues(0).trim
      val value = if (null == keyValues(1)) null
      else decode(keyValues(1).trim, "gbk")
      //转换成Map 分拣
      if (!parameterMapValues.contains(key)) parameterMapValues+=(key -> value)
    }
    parameterMapValues
  }
  def close()={
    inputStream.close()
  }
}