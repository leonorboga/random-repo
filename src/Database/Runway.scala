package Database

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

  def getRunwaysFromAirportCode(airportCode: String): RDD[Array[String]] = {

    val airports = getRunwaysFile

    airports.mapPartitions(lines => {
      val parser = new CSVParser(',')
      lines.filter(line => {
        val columns = parser.parseLine(line)
        Array(columns(airportRefColumn)).mkString("").contentEquals(airportCode)
      }).map(line => {
        val columns = parser.parseLine(line)
        Array(columns(lengthFtColumn),
          columns(widthFtColumn),
          columns(surfaceColumn))
      })
    })
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
