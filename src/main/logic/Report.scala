package main.logic

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

    reportOutput.append("Countries with highest number of airports (with count): ")
    reportOutput.append(newLine)
    val countriesMoreAirports = Airport.getCountriesOrderedByNumberOfAirports.sortBy(-_._2).take(numberOfResults)
    val countriesWithNameMoreAirports = countriesMoreAirports.map(x => Tuple2(Country.getCountryNameFromCode(x._1), x._2))

    countriesWithNameMoreAirports.foreach(country => {
      reportOutput.append(country._1).append(": ")
      reportOutput.append(country._2)
      reportOutput.append(newLine)
    })

    reportOutput.append(newLine)
    reportOutput.append(newLine)
    reportOutput.appendAll("Countries with lowest number of airports (with count): ")
    reportOutput.append(newLine)
    val countriesLessAirports = Airport.getCountriesOrderedByNumberOfAirports.sortBy(_._2).take(numberOfResults)
    val countriesWithNameLessAirports = countriesLessAirports.map(x => Tuple2(Country.getCountryNameFromCode(x._1), x._2))

    countriesWithNameLessAirports.foreach(country => {
      reportOutput.append(country._1).append(": ")
      reportOutput.append(country._2)
      reportOutput.append(newLine)
    })

    reportOutput.toString()
  }

  def getOrderedRunwayLatitudes(numberOfResults:Int): String =
  {
    val reportOutput:StringBuilder = new StringBuilder()
    reportOutput.append(Runway.getListOfOrderedLatitudes.take(numberOfResults).map(x => x._1).mkString(","))
    reportOutput.toString()
  }

  def getTypeOfRunwaysPerCountry: String = {
    val reportOutput:StringBuilder = new StringBuilder()
    reportOutput.append(getRunwaysPerCountry)
    reportOutput.toString()
  }

    private def getRunwaysPerCountry: String = {

      val reportOutput:StringBuilder = new StringBuilder()

      val runways = Runway.getRunwaysGroupedByAirport.sortBy(x => x._1)
      val airports = Airport.getListOfAirportAndCountry.sortBy(x => x._1).collect().toList

     // replaces the code of the airport by the code of the country where the airport is situated
      val runwaysAndCountriesCodes = runways.map(line => Tuple2(getCountryAirport(airports, line._1), line._2))
      val runwaysGroupedByCountryCode = runwaysAndCountriesCodes.groupByKey().map(x => (x._1, x._2.toList.flatten.distinct))

      val runwaysGroupedByCountryName = runwaysGroupedByCountryCode.map(x => Tuple2(Country.getCountryNameFromCode(x._1), x._2))

      runwaysGroupedByCountryName.collect().foreach(country => {
        reportOutput.append(country._1).append(": ")
        reportOutput.append(country._2.mkString(", "))
        reportOutput.append(newLine)
      })

      reportOutput.toString()
  }

  private def getCountryAirport(list:List[(String,String)], airportCode:String): String = {
    val tupleWithCountry = list.filter(x => x._1.equals(airportCode)).head
    tupleWithCountry._2
  }
}
