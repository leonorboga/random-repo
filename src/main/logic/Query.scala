package main.logic

import main.database.{Airport, Country, Runway}

/**
  * Implements the query option
  *
  * Query Option will ask the user for the country name or code and
  * print the airports & runways at each airport. The input can be
  * country code or country name.
  */
object Query {

  val airportIDColumn = 0
  val airportTypeColumn = 1
  val airportNameColumn = 2
  val airportMunicipalityColumn = 3

  val countryIDColumn = 0
  val countryNameColumn = 1

  val runwayLengthFtColumn = 0
  val runwayWidthFtColumn = 1
  val runwaySurfaceTypeColumn = 2
  val runwayLightedColumn = 3

  val newLine = sys.props("line.separator")

  def queryFromCountryName(countryName: String): String ={

    val queryOutput:StringBuilder = new StringBuilder()
    val countryCodesAndNames = Country.getCountryCodeFromName(countryName)

    countryCodesAndNames.collect().foreach(country => {

      queryOutput.append(getListOfAirports(country(countryIDColumn), country(countryNameColumn)))
    })

    queryOutput.toString()
  }

  def queryFromCountryCode(countryCode: String): String = {

    val queryOutput:StringBuilder = new StringBuilder()

    if(Country.checkIfCountryCodeExists(countryCode)) {

      val countryName = Country.getCountryNameFromCode(countryCode)
      queryOutput.append(getListOfAirports(countryCode, countryName))
    }

    queryOutput.toString()
  }

  private def getListOfAirports(countryCode: String, countryName:String): String = {
    val queryOutput:StringBuilder = new StringBuilder()

    val listOfAirports = Airport.getAirportsFromCountryCode(countryCode)

    listOfAirports.collect().foreach(airport => {

      queryOutput.append("Country: ")
      queryOutput.append(countryName).append(newLine)
      queryOutput.append("Municipality: ")
      queryOutput.append(airport(airportMunicipalityColumn)).append(newLine)
      queryOutput.append("Name: ")
      queryOutput.append(airport(airportNameColumn)).append(newLine)
      queryOutput.append("Type: ")
      queryOutput.append(airport(airportTypeColumn)).append(newLine)
      queryOutput.append("Runways: ")
      queryOutput.append(newLine)

      val runways = Runway.getRunwaysFromAirportCode(airport(airportIDColumn))
      runways.collect().foreach(runway => {

        queryOutput.append("Surface Type: ")
        queryOutput.append(runway(runwaySurfaceTypeColumn)).append(newLine)
        queryOutput.append("Length (ft): ")
        queryOutput.append(runway(runwayLengthFtColumn)).append(newLine)
        queryOutput.append("Width (ft): ")
        queryOutput.append(runway(runwayWidthFtColumn)).append(newLine)
        queryOutput.append("Lighted: ")
        queryOutput.append(runway(runwayLightedColumn)).append(newLine)
        queryOutput.append(newLine)
      })

      queryOutput.append(newLine)
    })

    queryOutput.toString()
  }
}
