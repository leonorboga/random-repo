package main.database

import au.com.bytecode.opencsv.CSVParser
import java.io.File

import org.apache.spark.rdd.RDD
import Utilities.readFile

/**
  * Query the Runways' database
  */
object Runway {

  var file:RDD[String] = null
  val airportRefColumn = 1
  val lengthFtColumn = 3
  val widthFtColumn = 4
  val surfaceColumn = 5
  val lightedColumn = 6
  val latitudeColumn = 8

  def getRunwaysFromAirportCode(airportCode: String): RDD[Array[String]] = {

    val runways = getRunwaysFile

    runways.mapPartitions(lines => {
      val parser = new CSVParser(',')
      lines.filter(line => {
        val columns = parser.parseLine(line)
        Array(columns(airportRefColumn)).mkString("").contentEquals(airportCode)
      }).map(line => {
        val columns = parser.parseLine(line)
        Array(columns(lengthFtColumn),
          columns(widthFtColumn),
          columns(surfaceColumn),
          if (columns(lightedColumn).equals("0")) "false" else "true")
      })
    })
  }

  def getRunwaysGroupedByAirport: RDD[(String,List[String])] = {

    val runways = getRunwaysFile

    runways.mapPartitions(lines => {
      val parser = new CSVParser(',')
      lines.map(line => {
        val columns = parser.parseLine(line)
        Tuple2(columns(airportRefColumn),
          columns(surfaceColumn).toUpperCase)
      })
    }).groupByKey().map(x => (x._1, x._2.toList))
  }

  def getListOfLatitudes: List[(String, Long)] = {
    val runways = getRunwaysFile

    runways.mapPartitions(lines => {
      val parser = new CSVParser(',')
      lines.map(line => {
        val columns = parser.parseLine(line)
         columns(latitudeColumn)
      })
    }).countByValue().toList
  }


  private def getRunwaysFile: RDD[String] ={
    if(file == null)
    {
      file = loadRunwaysFile()
    }
    file
  }

  private def loadRunwaysFile(): RDD[String] ={
    val currentDir = new File(".").getAbsolutePath
    readFile(currentDir + "/resources/runways.csv")
  }
}
