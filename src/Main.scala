import main.logic.{Query, Report}
import main.userInterface.AirportInformation

/**
  * Created by root on 4/10/16.
  */


object Main {
  def main(args: Array[String]): Unit = {

    new AirportInformation()

    /**
    while (true) {

      println("")
      println("")
      println("Welcome to our system!")
      println("")
      println("Select make a selection:")
      println("1 - Query from country name")
      println("2 - Query from country code")
      println("3 - Report")
      println("")
      println("Press something else in order to quit")
      println("")
      print("Option number: ")

      val option = readLine()

      option match {
        case "1" => println("Please introduce country name:")
          optionQuery(option)
        case "2" => println("Please introduce country code:")
          optionQuery(option)
        case "3" => println(Report.report())
        case _ => return
      }
    }
  }

    private def optionQuery(option: String): Unit = {
      val input = readLine()

      option match {
        case "1" => Query.queryFromCountryName(input) match {
          case Left(msg) => println(msg)
          case Right(output) => println(output)
        }
        case "2" =>
          val output = Query.queryFromCountryCode(input)
          println(output)
      }
      */
    }

}
