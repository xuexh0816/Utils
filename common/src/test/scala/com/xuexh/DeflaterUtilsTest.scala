package com.xuexh

/**
 * author: Xue Xionghui
 * crt_time: 2022/1/27 17:21
 * description: 
 */
object DeflaterUtilsTest {
  def main(args: Array[String]): Unit = {
    println(DeflaterUtils.zipString("Hello World"))
    println(DeflaterUtils.unzipString("eNrzSM3JyVcIzy/KSQEAGAsEHQ=="))
  }
}
