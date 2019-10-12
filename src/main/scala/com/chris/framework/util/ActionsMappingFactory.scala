package com.chris.framework.util

import scala.collection.mutable.Map
import scala.util.matching.Regex
object ActionsMappingFactory {
  val runtime = scala.reflect.runtime.universe
  private var actions:Map[String,Map[runtime.MethodMirror,Array[String]]] =  Map()
  def createAction(className:String): Unit ={
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
          val methodObj = methods.symbol.typeSignature.member(termTag).asMethod
          val realMethod = obj.reflectMethod(methodObj)
          actions+=( actionName -> Map(realMethod->Array[String]()))
          methodObj.paramLists.foreach(z=>
            z.foreach(u=>
              u.annotations.foreach(v=>
                if(v.tree.tpe.toString=="com.chris.framework.annotation.RequestAttribute"){
                  if(v.toString.contains(",")){
                    v.toString.split(",").foreach(str=>actions(actionName)(realMethod)=actions(actionName)(realMethod).:+(str.split("\"")(1)))
                  }
                  else
                    actions(actionName)(realMethod)=actions(actionName)(realMethod).:+(v.toString.split("\"")(1))
                }
              )
            )
          )
        }
      )
    )
  }
  def getAllActions() ={
    ClassesCollector.getClassPathList().foreach(x => createAction(x))
    this
  }
  def getAction(name:String):Map[String,Map[runtime.MethodMirror,Array[String]]]={
    if(actions.contains(name)){
      Map(name->actions(name))
    }
    else
      null
  }

}