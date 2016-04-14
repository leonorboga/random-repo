package Logic

import Database.{Airport, Country, Runway}

/**
  * Implements the option report
  */
object Report {

  val reportEntries = 10

  /**
    *  Choosing Reports will print the following:
    * - 10 countries with highest number of airports (with count) and
    * countries with lowest number of airports.
    * - Type of runways (as indicated in "surface" column) per country
    * - Bonus points: Print the top 10 most common runway latitude
    * (indicated in "le_ident" column)
    */

  def report(): String = {

    val reportOutput:StringBuilder = new StringBuilder()

    reportOutput.append("10 countries with highest number of airports (with count): ")
    reportOutput.append(sys.props("line.separator"))
    var countriesMoreAirports = Airport.getCountriesOrderedByNumberOfAirports.sortBy(-_._2).take(reportEntries)
    countriesMoreAirports = countriesMoreAirports.map(x => new Tuple2(Country.getCountryNameFromCode(x._1), x._2))
    reportOutput.append(countriesMoreAirports)

    reportOutput.append(sys.props("line.separator"))
    reportOutput.append(sys.props("line.separator"))
    reportOutput.appendAll("10 countries with lowest number of airports (with count): ")
    reportOutput.append(sys.props("line.separator"))
    var countriesLessAirports = Airport.getCountriesOrderedByNumberOfAirports.sortBy(_._2).take(reportEntries)
    countriesLessAirports = countriesLessAirports.map(x => new Tuple2(Country.getCountryNameFromCode(x._1), x._2))
    reportOutput.append(countriesLessAirports)

    reportOutput.append(sys.props("line.separator"))
    reportOutput.append(sys.props("line.separator"))
    reportOutput.appendAll("Types of runway per country")
    reportOutput.append(sys.props("line.separator"))
    reportOutput.append(getRunwaysPerCountry)

    reportOutput.append(sys.props("line.separator"))
    reportOutput.append(sys.props("line.separator"))
    reportOutput.appendAll("Print the top 10 most common runway latitude")
    reportOutput.append(sys.props("line.separator"))
    reportOutput.append(Runway.getListOfOrderedLatitudes.take(10))
//    reportOutput.append(Runway.getListOfOrderedLatitudes.take(10).map(x => x._1).mkString(","))

    reportOutput.toString()
  }


  private def getRunwaysPerCountry: String = {

    val queryOutput:StringBuilder = new StringBuilder()

    val runways = Runway.getRunwaysGroupedByAirport.sortBy(x => x._1)
    //queryOutput.append(runways.collect().mkString(sys.props("line.separator")))

    val airports = Airport.getListOfAirportAndCountry.sortBy(x => x._1).collect().toList

   // replaces the code of the airport by the code of the country where the airport is situated
    val runwaysAndCountries = runways.map(line => Tuple2(getCountryAirport(airports, line._1), line._2))
    val runwaysGroupedByCountry = runwaysAndCountries.groupByKey().map(x => (x._1, x._2.toList.flatten.distinct))

    queryOutput.append(runwaysGroupedByCountry.collect().mkString("\n"))
    queryOutput.toString()
  }

  private def getCountryAirport(list:List[(String,String)], airportCode:String): String = {
    val tupleWithCountry = list.filter(x => x._1.equals(airportCode)).head
    tupleWithCountry._2
  }


  private def getListOfAirports(countryCode: String): String = {
    val queryOutput:StringBuilder = new StringBuilder()

    val listOfAirports = Airport.getAirportsFromCountryCode(countryCode)

    listOfAirports.collect().foreach(airport => {
      queryOutput.append(" | ")
      queryOutput.append(airport(1)).append(" ")
      queryOutput.append(airport(2)).append(" ")
      queryOutput.append(airport(3)).append(" ")
      queryOutput.append(" runways: ")

      val runways = Runway.getRunwaysFromAirportCode(airport(0))
      runways.collect().foreach(runway => queryOutput.append(" r: ").append(runway.mkString(" ")))
    })

    queryOutput.toString()
  }

}
