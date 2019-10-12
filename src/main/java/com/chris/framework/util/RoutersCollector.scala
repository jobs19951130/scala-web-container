package com.chris.framework.util

import java.io.File

object RoutersCollector {

  def getClassPathList(args: Array[String]): List[String] = {
    var classList:List[String] = List[String]()
    val file:File = new File(this.getClass.getResource("/").getPath)
    getAllChildList(file).foreach(f =>
      if(f.getName.endsWith(".class")){
        var path = f.getPath
        path = path.substring(this.getClass.getResource("/").getPath.length-1)
        path = path.replace("\\",".")
        path = path.replace(".class","")
        classList = classList.:+(path)
      })
    classList

  }

  def getAllChildList(dir:File): Iterator[File]= {
    val listFiles: Array[File] = dir.listFiles()
    val isDirectory = listFiles.filter(x => x.isDirectory)
    val isFile: Array[File] = listFiles.filter(x => x.isFile)
    isFile.++(isDirectory.iterator.flatMap(x => getAllChildList(x))).iterator
  }

}