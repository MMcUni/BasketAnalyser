import scala.annotation.tailrec
import scala.io.StdIn.readLine
import analysis.PriceAnalyser
import scala.util.{Failure, Success, Try, Using}
import scala.io.Source
import java.io.IOException

/**
 * The main application object.
 *
 * This object is the entry point of the program. It reads data using DataManager and starts the application loop.
 */
object Main extends App {

  // Attempt to read data and handle success or failure
  DataManager.readData() match {
    case Success(data) => runAppLoop(data)  // Replace runApp with runAppLoop
    case Failure(exception) => println(s"Error reading data: ${exception.getMessage}")
  }


  /**
   * Runs the main application loop.
   * The method is tail-recursive to handle the repetitive user interaction.
   */
  @tailrec
  private def runAppLoop(data: Map[String, List[Int]]): Unit = {
    MenuManager.displayMainMenu()
    readLine() match {
      case "1" => PriceAnalysisManager.performPriceAnalysis(data); runAppLoop(data)
      case "2" => ShoppingManager.goShopping(data); runAppLoop(data)
      case "3" => // Exits the loop, effectively stopping the application
      case _ => println("Invalid choice. Please try again."); runAppLoop(data)
    }
  }
}

/**
 * Manages the display of various menus in the application.
 */
object MenuManager {

  /**
   * Displays the main menu of the application.
   */
  def displayMainMenu(): Unit = {
    println("\nMain Menu:")
    println("1. Price Analysis")
    println("2. Go Shopping!")
    println("3. Exit")
    print("Enter your choice: ")
  }

  /**
   * Displays the menu for price analysis.
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
}

/**
 * DataManager is responsible for reading and parsing data from a file.
 */
object DataManager {
  var filename = "src/main/data.txt"

  /**
   * Reads data from a file and parses it into a Map.
   *
   * @param filename The name of the file to read from.
   * @return A Try containing either the parsed data or an exception.
   */
  def readData(filename: String = DataManager.filename): Try[Map[String, List[Int]]] = {
    Using(Source.fromFile(filename)) { source =>
      source.getLines().map(parseLine).collect {
        case Some(parsedLine) => parsedLine  // Collect only successfully parsed lines
      }.toMap
    }
  }


  /**
   * Parses a line from the file into a key-value pair.
   *
   * @param line A line from the file.
   * @return An Option containing the parsed line, or None if parsing fails.
   */
  def parseLine(line: String): Option[(String, List[Int])] = {
    val parts = line.split(", ").toList
    if (parts.size < 2 || parts.tail.exists(!_.forall(_.isDigit))) {
      println(s"Warning: Skipping invalid line format: '$line'")
      None  // Return None if line format is invalid
    } else {
      Some(parts.head, parts.tail.map(_.toInt))
    }
  }
}

/**
 * PriceAnalysisManager handles the execution of different price analyses.
 */
object PriceAnalysisManager {
  /**
   * Coordinates and performs the chosen price analysis.
   *
   * @param data The data to analyze.
   */
  def performPriceAnalysis(data: Map[String, List[Int]]): Unit = {
    var continueAnalysis = true
    while (continueAnalysis) {
      MenuManager.displayPriceAnalysisMenu()
      try {
        val choice = readLine()

        // Handle user's choice for different analyses
        choice match {
          case "1" => displayCurrentPrices(data)
          case "2" => displayPriceRange(data)
          case "3" => displayMedianPrices(data)
          case "4" => displayLargestPriceIncrease(data)
          case "5" => compareAveragePrices(data)
          case "6" => continueAnalysis = false // Exit the analysis loop
          case _ => println("Invalid choice. Please try again.")
        }
      } catch {
        case e: IOException => println("Error reading input. Please try again.")
        case _: Throwable => println("An unexpected error occurred.")
      }
    }
  }

  /**
   * Displays the current food prices to the console.
   */
  def displayCurrentPrices(data: Map[String, List[Int]]): Unit = {
    val currentPrices = PriceAnalyser.getCurrentPrices(data)
    println("\nCurrent Food Prices:")
    currentPrices.foreach { case (food, price) =>
      println(s"$food: £${price.toDouble / 100}")
    }
  }

  /**
   * Displays the price range (minimum and maximum prices) for each food item to the console.
   */
  def displayPriceRange(data: Map[String, List[Int]]): Unit = {
    val minMaxPrices = PriceAnalyser.getMinMaxPrices(data)
    println("\nPrice Range (Min and Max) for Each Food Item:")
    minMaxPrices.foreach { case (food, (min, max)) => println(s"$food: Min = £${min.toDouble / 100}, Max = £${max.toDouble / 100}") }
  }

  /**
   * Displays median prices for each food item in the data.
   *
   * @param data The map of food items to their list of prices.
   */
  def displayMedianPrices(data: Map[String, List[Int]]): Unit = {
    if (data.isEmpty) {
      println("No data available to display median prices.")
    } else {
      println("\nMedian Prices for Each Food Item:")
      data.foreach { case (food, prices) =>
        if (prices.isEmpty) {
          println(s"Warning: No price data available for $food.")
        } else {
          val medianPrice = PriceAnalyser.getMedianPrice(prices)
          println(s"$food: Median Price = £${medianPrice.toDouble / 100}")
        }
      }
    }
  }

  /**
   * Displays the food item with the largest price increase over the last 6 months.
   *
   * @param data The map of food items to their list of prices.
   */
  def displayLargestPriceIncrease(data: Map[String, List[Int]]): Unit = {
    val (food, increase) = PriceAnalyser.getLargestPriceIncrease(data)
    println(s"\nLargest Price Increase in Last 6 Months:")
    println(s"$food with an increase of £${increase.toDouble / 100}")
  }

  /**
   * Displays a list of food items with their corresponding index numbers.
   *
   * @param foodItems The list of food item names.
   */
  def displayFoodListWithNumbers(foodItems: List[String]): Unit = {
    println("\nAvailable Food Items:")
    foodItems.zipWithIndex.foreach { case (food, index) =>
      println(s"${index + 1}. $food")
    }
  }

  /**
   * Allows the user to compare the average prices of two selected food items over a 2-year period.
   *
   * @param data The map of food items to their list of prices.
   */
  def compareAveragePrices(data: Map[String, List[Int]]): Unit = {
    val foodItems = data.keys.toList
    displayFoodListWithNumbers(foodItems)

    try {
      println("\nEnter the number for the first food item:")
      val firstIndex = scala.io.StdIn.readInt() - 1
      println("Enter the number for the second food item:")
      val secondIndex = scala.io.StdIn.readInt() - 1

      val firstFood = foodItems.lift(firstIndex).getOrElse("Unknown")
      val secondFood = foodItems.lift(secondIndex).getOrElse("Unknown")

      if (firstFood == "Unknown" || secondFood == "Unknown") {
        println("Invalid selection. Please enter valid food item numbers.")
      } else {
        val firstAvg = data.get(firstFood).map(PriceAnalyser.getAveragePrice).getOrElse(0.0)
        val secondAvg = data.get(secondFood).map(PriceAnalyser.getAveragePrice).getOrElse(0.0)

        println(s"\nAverage Price Comparison:")
        println(f"$firstFood: £${firstAvg / 100}%.2f")
        println(f"$secondFood: £${secondAvg / 100}%.2f")

        // Summary of the comparison
        if (firstAvg != 0.0 && secondAvg != 0.0) {
          val difference = (firstAvg - secondAvg).abs
          val summary = if (firstAvg > secondAvg) {
            f"Over the last 2 years, $firstFood is £${difference / 100}%.2f more expensive than $secondFood."
          } else if (secondAvg > firstAvg) {
            f"Over the last 2 years, $secondFood is £${difference / 100}%.2f more expensive than $firstFood."
          } else {
            s"Over the last 2 years, $firstFood and $secondFood have the same average price."
          }
          println(summary)
        }
      }
    } catch {
      case _: NumberFormatException => println("Invalid input. Please enter a number.")
    }
  }
}

/**
 * The ShoppingManager object is responsible for handling the shopping functionalities.
 */
object ShoppingManager {

  /**
   * Initiates the shopping process, allowing users to select food items and quantities.
   *
   * @param data The map of food items to their list of prices.
   */
  def goShopping(data: Map[String, List[Int]]): Unit = {
    val basket = scala.collection.mutable.Map[String, Float]()
    val currentPrices = PriceAnalyser.getCurrentPrices(data)

    println("\nGo Shopping!")
    displayFoodListWithPrices(data.keys.toList, currentPrices)

    var shoppingOption = "continue"
    while (shoppingOption != "back" && shoppingOption != "exit") {
      shoppingOption = handleShopping(basket, data, currentPrices)

      if (shoppingOption == "clear") {
        basket.clear()  // Clear the basket
        println("\nReturning to main menu with an empty basket...")
        return  // Return to main menu
      }
    }
  }

  /**
   * Handles the user's shopping choices, including item selection and quantity.
   *
   * @param basket        The current shopping basket.
   * @param data          The map of food items to their list of prices.
   * @param currentPrices The map of food items to their current prices.
   * @return A string indicating the next action (continue, back, exit).
   */
  private def handleShopping(basket: scala.collection.mutable.Map[String, Float], data: Map[String, List[Int]], currentPrices: Map[String, Int]): String = {
    println("\nEnter the number for the food item (or 'done' to finish):")
    val itemInput = readLine().trim

    itemInput.toLowerCase match {
      case "done" =>
        calculateAndDisplayBasketTotal(basket.toMap, data)
        basketOptions(basket, data)
      case _ =>
        Try(itemInput.toInt - 1) match {
          case Success(itemNumber) if data.keys.toList.isDefinedAt(itemNumber) =>
            val item = data.keys.toList(itemNumber)
            println(s"Enter the quantity for $item:")
            val quantityInput = readLine().trim
            Try(quantityInput.toFloat) match {
              case Success(quantity) if quantity > 0 =>
                // Update the basket with the selected item and quantity
                basket.updateWith(item) {
                  case Some(existingQuantity) => Some(existingQuantity + quantity)
                  case None => Some(quantity)
                }
                // Display the running total of the basket after adding the item
                calculateAndDisplayBasketTotal(basket.toMap, data)
                "continue"
              case _ =>
                println("Invalid quantity. Please enter a positive number.")
                "continue"
            }
          case _ =>
            println("Invalid item number. Please enter a valid number.")
            "continue"
        }
    }
  }

  /**
   * Presents various options to the user for their shopping basket and handles the user's choice.
   *
   * @param basket The current shopping basket with items and quantities.
   * @param data   The map of food items to their list of prices.
   * @return A string indicating the next action (edit, clear, back, exit, or invalid).
   */
  def basketOptions(basket: scala.collection.mutable.Map[String, Float], data: Map[String, List[Int]]): String = { //public for testing
    println("\nBasket Options:")
    println("1. Pay Now")
    println("2. Edit Basket")
    println("3. Clear Basket")
    println("4. Back")
    println("5. Exit")
    print("Enter your choice: ")

    readLine().trim match {
      case "1" =>
        // Special message for a promotional event
        println("\nYou're our 1 billionth user and don't need to pay! Your order is on the way!")
        println("1. Main Menu")
        println("2. Exit")
        val postPaymentChoice = readLine().trim
        if (postPaymentChoice == "2") "exit" else "back"
      case "2" => "edit"
      case "3" =>
        // Clearing the current shopping basket
        basket.clear()
        println("\nBasket has been cleared.")
        "clear"
      case "4" => "back"
      case "5" => "exit"
      case _ =>
        println("Invalid choice. Please try again.")
        "invalid"
    }
  }

  /**
   * Displays a list of available food items along with their current prices.
   *
   * @param foodItems A list of food item names.
   * @param prices    The current prices of the food items.
   */
  private def displayFoodListWithPrices(foodItems: List[String], prices: Map[String, Int]): Unit = {
    println("\nAvailable Food Items with Current Prices:")
    foodItems.zipWithIndex.foreach { case (food, index) =>
      val price = prices.getOrElse(food, 0) / 100.0
      println(s"${index + 1}. $food - £$price")
    }
  }

  /**
   * Calculates and displays the total value of the items in the shopping basket.
   *
   * @param basket The shopping basket with items and their quantities.
   * @param data   The map of food items to their list of prices.
   */
  private def calculateAndDisplayBasketTotal(basket: Map[String, Float], data: Map[String, List[Int]]): Unit = {
    val currentPrices = PriceAnalyser.getCurrentPrices(data)
    // Calculating the total value of the basket
    val totalValue = basket.foldLeft(0f) { case (total, (item, quantity)) =>
      currentPrices.get(item) match {
        case Some(price) => total + price * quantity
        case None =>
          println(s"Warning: Item '$item' not recognized.")
          total
      }
    }
    println(f"Total value of your basket: £${totalValue / 100}")
  }
}