package Database

import Configuration.Spark
import org.apache.spark.rdd.RDD

/**
  *
  */
object Utilities {
  def readFile(fileName: String):  RDD[String] = {
    val fileData = Spark.spark().textFile(fileName).cache()
    dropHeader(fileData)
  }

  def dropHeader(data: RDD[String]): RDD[String] = {
    data.mapPartitionsWithIndex((idx, lines) => {
      if (idx == 0) {
        lines.drop(1)
      }
      lines
    })
  }

}
