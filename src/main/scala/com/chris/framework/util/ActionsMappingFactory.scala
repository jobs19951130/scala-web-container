package com.chris.framework.util

import scala.collection.mutable.Map
object ActionsMappingFactory {
  val runtime = scala.reflect.runtime.universe
  var actions:Map[String,Map[runtime.MethodMirror,Array[String]]] =  Map()
  def createAction(className:String): Unit ={
    println(className)
    val mirror = runtime.runtimeMirror(getClass.getClassLoader)
    val objMirror = mirror.staticModule(className)
    val methods = mirror.reflectModule(objMirror)
    val obj = mirror.reflect(methods.instance)
    if(!obj.symbol.annotations.toString.contains("com.chris.framework.annotation.MyController")){
      return
    }
    obj.symbol.typeSignature.members.foreach(x=>
      x.annotations.foreach(y=>
        if(y.tree.tpe.toString=="com.chris.framework.annotation.MyMapping"){
          val termTag = runtime.TermName(x.toString.split(" ").apply(1))
          val actionName = y.toString.split("\"").apply(1)
          println(termTag+"------"+actionName)
          val methodObj = methods.symbol.typeSignature.member(termTag).asMethod
          val realMethod = obj.reflectMethod(methodObj)
          actions+=( actionName -> Map(realMethod->Array[String]()))
          methodObj.paramLists.foreach(z=>
            z.foreach(u=>
              u.annotations.foreach(v=>
                if(v.tree.tpe.toString=="com.chris.framework.annotation.RequestAttribute"){
                  actions(actionName)(realMethod)=actions(actionName)(realMethod).:+(v.toString.split("\"").toBuffer.drop(1).dropRight(1)(0))
                }
              )
            )
          )
        }
      )
    )
  }

  def scanController()={
    ClassesCollector.getClassPathList().foreach(x => createAction(x))
    println(actions)
  }
  def main(args: Array[String]): Unit = {
    scanController()
  }
}