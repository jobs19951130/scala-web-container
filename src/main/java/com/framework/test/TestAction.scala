package com.framework.test

import com.chris.framework.annotation.MyController
import com.chris.framework.annotation.MyMapping
import com.chris.framework.annotation.RequestAttribute
@MyController
object TestAction {
  @MyMapping("/5")
  def test(@RequestAttribute("ss") ss:String): Unit ={
    println("2323")
  }
}
