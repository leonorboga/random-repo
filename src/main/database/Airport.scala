package main.database

import java.io.File

import au.com.bytecode.opencsv.CSVParser
import org.apache.spark.rdd.RDD
import Utilities.readFile

/**
  * Query the Airports' main.database
  */

object Airport {

  var file:RDD[String] = null
  val airportIDColumn = 0
  val airportTypeColumn = 2
  val airportNameColumn = 3
  val isoCountryColumn = 8
  val airportMunicipalityColumn = 10

  def getAirportsFromCountryCode(countryCode: String): RDD[Array[String]] = {

    val airports = getAirportsFile

    airports.mapPartitions(lines => {
      val parser = new CSVParser(',')
      lines.filter(line => {
        val columns = parser.parseLine(line)
        Array(columns(isoCountryColumn)).mkString("").contentEquals(countryCode)
      }).map(line => {
        val columns = parser.parseLine(line)
        Array(columns(airportIDColumn),
          columns(airportTypeColumn),
          columns(airportNameColumn),
          columns(airportMunicipalityColumn))
      })
    })
  }

  def getCountriesByNumberOfAirports: List[(String, Long)] = {

    val airports = getAirportsFile

    airports.mapPartitions(lines => {
      val parser = new CSVParser(',')
      lines.map(line => {
        val columns = parser.parseLine(line)
        Array(columns(isoCountryColumn)).mkString(",")
      })
    }).countByValue().toList
  }

  def getListOfAirportAndCountry: RDD[(String,String)] = {

    val airports = getAirportsFile

    airports.mapPartitions(lines => {
      val parser = new CSVParser(',')
      lines.map(line => {
        val columns = parser.parseLine(line)
        Tuple2(columns(airportIDColumn),
          columns(isoCountryColumn))
      })
    })
  }

  private def getAirportsFile: RDD[String] ={
    if(file == null)
    {
      file = loadAirportsFile()
    }
    file
  }

  private def loadAirportsFile(): RDD[String] ={
    val currentDir = new File(".").getAbsolutePath
    readFile(currentDir + "/resources/airports.csv")
  }
}
