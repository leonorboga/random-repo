package main.logic

import java.util.{Locale, ResourceBundle}

import main.database.{Airport, Country, Runway}

/**
  * Implements the option report
  */
object Report {
  val newLine = sys.props("line.separator")

  /**
    *  Choosing Reports will print the following:
    * - 10 countries with highest number of airports (with count) and
    * countries with lowest number of airports.
    * - Type of runways (as indicated in "surface" column) per country
    * - Bonus points: Print the top 10 most common runway latitude
    * (indicated in "le_ident" column)
    */

  def airportsPerCountryWithCount(numberOfResults:Int): String ={
    val reportOutput:StringBuilder = new StringBuilder()

    val countriesMoreAirports = Airport.getCountriesByNumberOfAirports.sortBy(-_._2).take(numberOfResults)
    val countriesWithNameMoreAirports = countriesMoreAirports.map(x => Tuple2(Country.getCountryNameFromCode(x._1), x._2))

    countriesWithNameMoreAirports.foreach(line => {
      reportOutput.append(line._1).append(": ")
      reportOutput.append(line._2)
      reportOutput.append(newLine)
    })

    reportOutput.append(newLine)
    val countriesLessAirports = Airport.getCountriesByNumberOfAirports.sortBy(_._2).take(numberOfResults)
    val countriesWithNameLessAirports = countriesLessAirports.map(x => Tuple2(Country.getCountryNameFromCode(x._1), x._2))

    countriesWithNameLessAirports.foreach(line => {
      reportOutput.append(line._1).append(": ")
      reportOutput.append(line._2)
      reportOutput.append(newLine)
    })

    reportOutput.toString()
  }

  def getOrderedRunwayLatitudes(numberOfResults:Int): String =
  {
    Runway.getListOfLatitudes.sortBy(-_._2).take(numberOfResults).map(x => x._1).mkString(", ")
  }

  def getRunwaysPerCountry: String = {

    val reportOutput:StringBuilder = new StringBuilder()

    val runways = Runway.getRunwaysGroupedByAirport.sortBy(x => x._1)
    val airports = Airport.getListOfAirportAndCountry.sortBy(x => x._1).collect().toList

   // replaces the code of the airport by the code of the country where the airport is situated
    val runwaysAndCountriesCodes = runways.map(line => Tuple2(getCountryAirport(airports, line._1), line._2))
    val runwaysGroupedByCountryCode = runwaysAndCountriesCodes.groupByKey().map(x => (x._1, x._2.toList.flatten.distinct))

    val runwaysGroupedByCountryName = runwaysGroupedByCountryCode.map(x => Tuple2(Country.getCountryNameFromCode(x._1), x._2))

    runwaysGroupedByCountryName.collect().foreach(line => {
      reportOutput.append(line._1).append(": ")
      reportOutput.append(line._2.mkString(", "))
      reportOutput.append(newLine)
    })

    reportOutput.toString()
  }

  private def getCountryAirport(airports:List[(String,String)], airportCode:String): String = {
    val tupleWithCountry = airports.filter(x => x._1.equals(airportCode)).head
    tupleWithCountry._2
  }
}
