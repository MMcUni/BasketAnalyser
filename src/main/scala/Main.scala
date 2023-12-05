import analysis.PriceAnalyzer
import scala.io.Source
import scala.util.{Try, Using, Success, Failure}
import scala.io.StdIn.readLine

/**
 * Main object serving as the entry point for the BasketAnalyzer application.
 * This application provides a menu-driven interface for analyzing food price data.
 */
object Main extends App {
  // Define the file path for the data file containing food prices.
  private val filename = "src/main/data.txt"

  /**
   * Read the data file and parse each line into a map of food items and their prices.
   * Each line in the file represents a food item followed by its prices over time.
   */
  private val data: Try[Map[String, List[Int]]] = Using(Source.fromFile(filename)) { source =>
    source.getLines().map(parseLine).toMap
  }

  /**
   * Parses a single line from the data file into a tuple.
   * The line is expected to be in the format: "ItemName, price1, price2, ..."
   *
   * @param line The line to parse.
   * @return A tuple of the food item name and a list of its prices.
   */
  private def parseLine(line: String): (String, List[Int]) = {
    val parts = line.split(", ")
    (parts.head, parts.tail.toList.map(_.toInt))
  }

  /**
   * Displays the main menu options to the console.
   */
  def displayMainMenu(): Unit = {
    println("\nMain Menu:")
    println("1. Price Analysis")
    println("2. Go Shopping!")
    println("3. Exit")
    print("Enter your choice: ")
  }

  /**
   * Displays the price analysis menu options to the console.
   */
  def displayPriceAnalysisMenu(): Unit = {
    println("\nPrice Analysis Menu:")
    println("1. Current Food Prices")
    println("2. Price Range (min/max)")
    println("3. Median Price in Period")
    println("4. Largest Price Increase in 6 Months")
    println("5. Compare 2-Year Prices")
    println("6. Back")
    print("Enter your choice: ")
  }

  /**
   * Handles the user interaction for price analysis.
   * Allows the user to select different types of price analysis and view the results.
   */
  def performPriceAnalysis(): Unit = {
    var continueAnalysis = true
    while (continueAnalysis) {
      displayPriceAnalysisMenu()
      val choice = readLine()

      choice match {
        case "1" => displayCurrentPrices()
        case "2" => displayPriceRange()
        case "3" => displayMedianPrices()
        case "4" => displayLargestPriceIncrease()
        case "5" => // Placeholder for 2-year price comparison functionality
        case "6" => continueAnalysis = false
        case _ => println("Invalid choice. Please try again.")
      }
    }
  }

  /**
   * Displays the current food prices to the console.
   */
  def displayCurrentPrices(): Unit = {
    data match {
      case Success(parsedData) =>
        val currentPrices = PriceAnalyzer.getCurrentPrices(parsedData)
        println("\nCurrent Food Prices:")
        currentPrices.foreach { case (food, price) => println(s"$food: $price") }
      case Failure(exception) =>
        println(s"An error occurred while processing the data: ${exception.getMessage}")
    }
  }

  /**
   * Displays the price range (minimum and maximum prices) for each food item to the console.
   */
  def displayPriceRange(): Unit = {
    data match {
      case Success(parsedData) =>
        val minMaxPrices = PriceAnalyzer.getMinMaxPrices(parsedData)
        println("\nPrice Range (Min and Max) for Each Food Item:")
        minMaxPrices.foreach { case (food, (min, max)) =>
          println(s"$food: Min = $min, Max = $max")
        }
      case Failure(exception) =>
        println(s"An error occurred while processing the data: ${exception.getMessage}")
    }
  }

  def displayMedianPrices(): Unit = {
    data match {
      case Success(parsedData) =>
        println("\nMedian Prices for Each Food Item:")
        parsedData.foreach { case (food, prices) =>
          val medianPrice = PriceAnalyzer.getMedianPrice(prices)
          println(s"$food: Median Price = $medianPrice")
        }
      case Failure(exception) =>
        println(s"An error occurred while processing the data: ${exception.getMessage}")
    }
  }

  def displayLargestPriceIncrease(): Unit = {
    data match {
      case Success(parsedData) =>
        val (food, increase) = PriceAnalyzer.getLargestPriceIncrease(parsedData)
        println(s"\nLargest Price Increase in Last 6 Months:")
        println(s"$food with an increase of $increase")
      case Failure(exception) =>
        println(s"An error occurred while processing the data: ${exception.getMessage}")
    }
  }

  /**
   * Placeholder for the Go Shopping feature, currently under construction.
   */
  def goShopping(): Unit = {
    println("Go Shopping feature is under construction.")
  }

  // Main loop for the application's menu-driven interface.
  var continueRunning = true
  while (continueRunning) {
    displayMainMenu()
    val choice = readLine()

    choice match {
      case "1" => performPriceAnalysis()
      case "2" => continueRunning = false  // Exit the application.
      case _ => println("Invalid choice. Please try again.")
    }
  }
}
