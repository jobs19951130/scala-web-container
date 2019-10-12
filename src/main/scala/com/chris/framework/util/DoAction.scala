package com.chris.framework.util

object DoAction {

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

}
