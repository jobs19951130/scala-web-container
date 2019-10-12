package com.framework.test

import com.chris.framework.annotation.{MyController, MyFrameworkApplication, MyMapping, RequestAttribute}
import scala.collection.mutable.ArrayBuffer
@MyController
@MyFrameworkApplication
object People{
  @MyMapping("/2")
  def speak(@RequestAttribute("name")word:String)={
    println(word)
  }
  @MyMapping("/3")
  def listen(@RequestAttribute("pwd")pwd:String, @RequestAttribute("user")user:String)={
    println(pwd)
  }
}
object A{}
class B{}
object MyFrameworkApp {
  val runtime= scala.reflect.runtime.universe
  def main(args: Array[String]): Unit = {
    val mirror = runtime.runtimeMirror(getClass.getClassLoader)
    val p = mirror.staticModule("com.framework.test.People")
    val m = mirror.reflectModule(p)
    val objm = mirror.reflect(m.instance)
    //m.symbol.typeSignature.members.foreach(x=>{x.annotations.foreach(y=>if(y.tree.tpe.toString=="com.chris.framework.annotation.MyMapping"){println(y)})})
    println(objm.symbol.annotations.toString.contains("com.chris.framework.annotation.MyController"))
    val method = m.symbol.typeSignature.member(runtime.TermName("speak")).asMethod  //反射调用函数
    var arr:ArrayBuffer[String] = null
    method.paramLists.foreach(x=>x.foreach(y=>y.annotations.foreach(z=>if(z.tree.tpe.toString=="com.chris.framework.annotation.RequestAttribute"){var temp = z.toString.split("\"").toBuffer
      println(temp.drop(1).dropRight(1)(0))})))
    println()
    //val result = objm.reflectMethod(method)("hello")

  }
}
