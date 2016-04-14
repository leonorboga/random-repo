package Logic

import Database.{Airport, Country, Runway}

import scala.util.{Either, Left, Right}

/**
  * Implements the query option
  */
object Query {

  /**
    * Query Option will ask the user for the country name or code and
    * print the airports & runways at each airport. The input can be
    * country code or country name.
    */

  def queryFromCountryName(countryName: String): Either[String, String] ={

    if(countryName.length < 3)
    {
      return Left("A country name has to have a minimum of 3 characters!")
    }

    val queryOutput:StringBuilder = new StringBuilder()
    val countryCodes = Country.getCountryCodeFromName(countryName)

    countryCodes.collect().foreach(countryCode => {

      queryOutput.append(getListOfAirports(countryCode))
    })

    Right(queryOutput.toString())
  }

  def queryFromCountryCode(countryCode: String): String = {

    val queryOutput:StringBuilder = new StringBuilder()

    if(Country.checkIfCountryCodeExists(countryCode)) {
      queryOutput.append(getListOfAirports(countryCode))
    }

    queryOutput.toString()
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
      runways.collect().foreach(r => queryOutput.append(" r: ").append(r.mkString(" ")))
    })

    queryOutput.toString()
  }
}
