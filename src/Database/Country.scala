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

  def getCountryCodeFromName(countryName: String): RDD[Array[String]] = {

    val countries = getCountriesFile

    countries.mapPartitions(lines => {
      val parser = new CSVParser(',')
      lines.filter(line => {
        val columns = parser.parseLine(line)
        columns(countryNameColumn).toLowerCase().startsWith(countryName.toLowerCase())
      }).map(line => {
        val columns = parser.parseLine(line)
        Array(columns(countryIDColumn),
          columns(countryNameColumn))
      })
    })
  }

  def checkIfCountryCodeExists(countryCode: String): Boolean = {

    val countries = getCountriesFile

    !countries.mapPartitions(lines => {
      val parser = new CSVParser(',')
      lines.filter(line => {
        val columns = parser.parseLine(line)
        columns(countryIDColumn).toLowerCase().equals(countryCode.toLowerCase())
      })
    }).isEmpty()
  }

  def getCountryNameFromCode(countryCode: String): String = {

    val countries = getCountriesFile

    countries.mapPartitions(lines => {
      val parser = new CSVParser(',')
      lines.filter(line => {
        val columns = parser.parseLine(line)
        columns(countryIDColumn).equals(countryCode)
      }).map(line => {
        val columns = parser.parseLine(line)
        columns(countryNameColumn)
      })
    }).first()
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
