import analysis.PriceAnalyzer
import scala.io.Source
import scala.util.{Try, Using, Success, Failure}
import scala.io.StdIn.readLine

/**
 * Main object serving as the entry point of the BasketAnalyzer application.
 */
object Main extends App {
  // Define the file path for the data file containing food prices.
  private val filename = "src/main/data.txt"

  // Read the data file and parse each line into a map of food items and their prices.
  private val data: Try[Map[String, List[Int]]] = Using(Source.fromFile(filename)) { source =>
    source.getLines().map(parseLine).toMap
  }

  /**
   * Parses a single line from the data file into a tuple.
   *
   * @param line The line to parse.
   * @return A tuple of the food item name and a list of its prices.
   */
  private def parseLine(line: String): (String, List[Int]) = {
    val parts = line.split(", ")
    (parts.head, parts.tail.toList.map(_.toInt))
  }

  /**
   * Displays the menu options to the console.
   */
  def displayMenu(): Unit = {
    println("\nMenu:")
    println("1. Price Analysis")
    println("2. Exit")
    print("Enter your choice: ")
  }

  /**
   * Performs price analysis by displaying current, minimum, and maximum prices.
   */
  def performPriceAnalysis(): Unit = {
    data match {
      case Success(parsedData) =>
        // Retrieve and display current prices.
        val currentPrices = PriceAnalyzer.getCurrentPrices(parsedData)
        println("\nCurrent Prices:")
        currentPrices.foreach { case (food, price) => println(s"$food: $price") }

        // Retrieve and display minimum and maximum prices.
        val minMaxPrices = PriceAnalyzer.getMinMaxPrices(parsedData)
        println("\nMin and Max Prices:")
        minMaxPrices.foreach { case (food, (min, max)) => println(s"$food: Min = $min, Max = $max") }

      case Failure(exception) =>
        println(s"An error occurred while reading the file: ${exception.getMessage}")
    }
  }

  // Main loop for the menu-driven interface.
  var continueRunning = true
  while (continueRunning) {
    displayMenu()
    val choice = readLine()

    choice match {
      case "1" => performPriceAnalysis()
      case "2" => continueRunning = false  // Exit the application.
      case _ => println("Invalid choice. Please try again.")
    }
  }
}
