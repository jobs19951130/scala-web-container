package com.chris.framework.util

import java.io.{File, FileInputStream, FileNotFoundException, IOException, UnsupportedEncodingException}
object DoAction {

  def response(request:Request,response:Response)={
    if(request.url.contains(".")){
      getPage(request,response)
    }
    else
      process(request,response)
  }
  def process(request:Request,response:Response)={
    val actionName = request.url
    val prame:Map[String,String] = request.body
    val actions = ActionsMappingFactory.getAllActions()
    val action = actions.getAction(actionName).take(1)
    val methodMap = action(actionName).take(1)
    val methodName = methodMap.keys.last
    var parameters:List[String] = List[String]()
    methodMap(methodName).foreach(x=>{
      val p = prame("\""+x+"\"").toString
      parameters=parameters.:+(p.substring(1,p.size-1))
    })
    response.print(methodName(parameters).toString)
    response.push2Client(200)
    response.closeIO()
  }

  def getPage(request:Request,response:Response)={
    val pagePath = request.url
    //this.getClass.getResourceAsStream("static/"+pagePath)
    println(pagePath)
    val file = new File(this.getClass.getResource("/static"+pagePath).getPath)
    val fileLength = file.length
    val fileContent = new Array[Byte](fileLength.intValue)
    try {
      val in = new FileInputStream(file)
      in.read(fileContent)
      in.close()
    } catch {
      case e: FileNotFoundException =>
        e.printStackTrace()
      case e: IOException =>
        e.printStackTrace()
    }
    val str = new String(fileContent, "UTF-8")
    response.println(str)
    response.push2Client(200)
    response.closeIO()
  }

}
