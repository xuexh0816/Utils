package com.xuexh

/**
 * author: Xue Xionghui
 * crt_time: 2022/2/9 14:09
 * description: 
 */
object PropUtilsTest {
  def main(args: Array[String]): Unit = {
    val map: Map[String, String] = PropUtils.fromPropertiesFile("/Users/xuexh/Documents/IdeaProjects/XuexhGitHub/xuexh-utils/common/src/main/resources/aa.properties")
    val option: Option[String] = map.get("aa")
    println(option.get)
  }

}
