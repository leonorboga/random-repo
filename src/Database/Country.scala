package Database

import au.com.bytecode.opencsv.CSVParser
import java.io.File
import org.apache.spark.rdd.RDD
import Utilities.readFile

/**
  * Query the Countries' database
  */
object Country {

  var file:RDD[String] = null
  val countryNameColumn = 2
  val countryIDColumn = 1

  def getCountryCodeFromName(countryName: String): RDD[String] = {

    val countries = getCountriesFile

    countries.mapPartitions(lines => {
      val parser = new CSVParser(',')
      lines.filter(line => {
        val columns = parser.parseLine(line)
        columns(countryNameColumn).toLowerCase().startsWith(countryName.toLowerCase())
      }).map(line => {
        val columns = parser.parseLine(line)
        columns(countryIDColumn)
      })
    })
  }

  def checkIfCountryCodeExists(countryCode: String): Boolean = {

    val countries = getCountriesFile

    !countries.mapPartitions(lines => {
      val parser = new CSVParser(',')
      lines.filter(line => {
        val columns = parser.parseLine(line)
        columns(countryIDColumn).contentEquals(countryCode)
      })
    }).isEmpty()
  }

  private def getCountriesFile: RDD[String] ={
    if(file == null)
    {
      file = loadCountriesFile()
    }
    file
  }

  private def loadCountriesFile(): RDD[String] ={
    val currentDir = new File(".").getAbsolutePath
     readFile(currentDir + "/resources/countries.csv")
  }
}
