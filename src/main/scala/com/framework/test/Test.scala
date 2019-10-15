package main.scala.com.framework.test

object Test {
  def main(args: Array[String]): Unit = {
    var a = "/index.html"
    println(a.split("/").last.split(".").last)
  }
}
