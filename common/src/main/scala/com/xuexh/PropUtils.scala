package com.xuexh

import org.yaml.snakeyaml.Yaml

import java.io._
import java.util.{Properties, Scanner}
import scala.collection.JavaConversions.{asScalaSet, mapAsScalaMap}
import scala.collection.mutable

/**
 * author: Xue Xionghui
 * crt_time: 2022/2/9 14:03
 * description: 
 */
object PropUtils {

  /**
   * @param filename AbsolutePath
   * @return java.lang.String
   * */
  def readFile(filename: String): String = {
    val file = new File(filename)
    require(file.exists(), s"readFile: file $file does not exist")
    require(file.isFile, s"file $file is not a normal file")
    val scanner = new Scanner(file)
    val buffer = new StringBuilder
    while (scanner.hasNextLine) {
      buffer.append(scanner.nextLine()).append("\r\n")
    }
    scanner.close()
    buffer.toString()
  }

  /**
   * @param filename AbsolutePath
   * @return scala.collection.immutable.Map<java.lang.String,java.lang.String>
   * */
  def fromYamlFile(filename: String): Map[String, String] = {
    val file = new File(filename)
    require(file.exists(), s"fromYamlFile: Yaml file $file does not exist")
    require(file.isFile, s"fromYamlFile: Yaml file $file is not a normal file")
    val inputStream: InputStream = new FileInputStream(file)
    try {
      val map: mutable.Map[String, String] = mutable.Map[String, String]()
      new Yaml()
        .load(inputStream)
        .asInstanceOf[java.util.Map[String, Map[String, Any]]]
        .flatMap(x => eachAppendYamlItem("", x._1, x._2, map)).toMap
    } catch {
      case e: IOException => throw new IllegalArgumentException(s"Failed when loading properties from yamlFile $filename", e)
    } finally {
      inputStream.close()
    }
  }

  def fromPropertiesFile(filename: String): Map[String, String] = {
    val file = new File(filename)
    require(file.exists(), s"fromPropertiesFile: Properties file $file does not exist")
    require(file.isFile, s"fromPropertiesFile: Properties file $file is not a normal file")

    val inReader = new InputStreamReader(new FileInputStream(file), "UTF-8")
    try {
      val properties = new Properties()
      properties.load(inReader)
      properties.stringPropertyNames().map(k => (k, properties.getProperty(k).trim)).toMap
    } catch {
      case e: IOException => throw new IllegalArgumentException(s"Failed when loading properties from propertyFile $filename", e)
    } finally {
      inReader.close()
    }
  }

  private[this] def eachAppendYamlItem(prefix: String, k: String, v: Any, proper: collection.mutable.Map[String, String]): Map[String, String] = {
    v match {
      case map: Map[String, Any] =>
        map.flatMap(x => {
          prefix match {
            case "" => eachAppendYamlItem(k, x._1, x._2, proper)
            case other => eachAppendYamlItem(s"$other.$k", x._1, x._2, proper)
          }
        })
      case text =>
        val value: String = text match {
          case null => ""
          case other => other.toString
        }
        prefix match {
          case "" => proper += k -> value
          case other => proper += s"$other.$k" -> value
        }
        proper.toMap
    }
  }
}