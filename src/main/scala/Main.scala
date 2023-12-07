import analysis.PriceAnalyzer
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}
import scala.io.StdIn.readLine

object Main extends App {
  DataManager.readData match {
    case Success(data) => runApp(data)
    case Failure(exception) => println(s"Error reading data: ${exception.getMessage}")
  }

  private def runApp(data: Map[String, List[Int]]): Unit = {
    var continueRunning = true
    while (continueRunning) {
      MenuManager.displayMainMenu()
      val choice = readLine()

      choice match {
        case "1" => PriceAnalysisManager.performPriceAnalysis(data)
        case "2" => ShoppingManager.goShopping(data)
        case "3" => continueRunning = false
        case _ => println("Invalid choice. Please try again.")
      }
    }
  }
}

object MenuManager {
  def displayMainMenu(): Unit = {
    println("\nMain Menu:")
    println("1. Price Analysis")
    println("2. Go Shopping!")
    println("3. Exit")
    print("Enter your choice: ")
  }

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

object DataManager {
  private val filename = "src/main/data.txt"

  def readData: Try[Map[String, List[Int]]] = {
    Using(Source.fromFile(filename)) { source =>
      source.getLines().map(parseLine).toMap
    }
  }

  private def parseLine(line: String): (String, List[Int]) = {
    val parts = line.split(", ")
    (parts.head, parts.tail.toList.map(_.toInt))
  }
}

object PriceAnalysisManager {
  def performPriceAnalysis(data: Map[String, List[Int]]): Unit = {
    var continueAnalysis = true
    while (continueAnalysis) {
      MenuManager.displayPriceAnalysisMenu()
      val choice = readLine()

      choice match {
        case "1" => displayCurrentPrices(data)
        case "2" => displayPriceRange(data)
        case "3" => displayMedianPrices(data)
        case "4" => displayLargestPriceIncrease(data)
        case "5" => compareAveragePrices(data)
        case "6" => continueAnalysis = false
        case _ => println("Invalid choice. Please try again.")
      }
    }

    /**
     * Displays the current food prices to the console.
     */
    def displayCurrentPrices(data: Map[String, List[Int]]): Unit = {
      val currentPrices = PriceAnalyzer.getCurrentPrices(data)
      println("\nCurrent Food Prices:")
      currentPrices.foreach { case (food, price) =>
        println(s"$food: £${price.toDouble / 100}")
      }
    }

    /**
     * Displays the price range (minimum and maximum prices) for each food item to the console.
     */
    def displayPriceRange(data: Map[String, List[Int]]): Unit = {
      val minMaxPrices = PriceAnalyzer.getMinMaxPrices(data)
      println("\nPrice Range (Min and Max) for Each Food Item:")
      minMaxPrices.foreach { case (food, (min, max)) => println(s"$food: Min = $min, Max = $max") }
    }


    def displayMedianPrices(data: Map[String, List[Int]]): Unit = {
      println("\nMedian Prices for Each Food Item:")
      data.foreach { case (food, prices) =>
        val medianPrice = PriceAnalyzer.getMedianPrice(prices)
        println(s"$food: Median Price = $medianPrice")
      }
    }


    def displayLargestPriceIncrease(data: Map[String, List[Int]]): Unit = {
      val (food, increase) = PriceAnalyzer.getLargestPriceIncrease(data)
      println(s"\nLargest Price Increase in Last 6 Months:")
      println(s"$food with an increase of $increase")
    }


    def displayFoodListWithNumbers(foodItems: List[String]): Unit = {
      println("\nAvailable Food Items:")
      foodItems.zipWithIndex.foreach { case (food, index) =>
        println(s"${index + 1}. $food")
      }
    }

    def compareAveragePrices(data: Map[String, List[Int]]): Unit = {
      val foodItems = data.keys.toList
      displayFoodListWithNumbers(foodItems)

      println("\nEnter the number for the first food item:")
      val firstIndex = scala.io.StdIn.readInt() - 1
      println("Enter the number for the second food item:")
      val secondIndex = scala.io.StdIn.readInt() - 1

      val firstFood = foodItems.lift(firstIndex).getOrElse("Unknown")
      val secondFood = foodItems.lift(secondIndex).getOrElse("Unknown")

      val firstAvg = data.get(firstFood).map(PriceAnalyzer.getAveragePrice).getOrElse(0.0)
      val secondAvg = data.get(secondFood).map(PriceAnalyzer.getAveragePrice).getOrElse(0.0)

      println(s"\nAverage Price Comparison:")
      println(s"$firstFood: $firstAvg")
      println(s"$secondFood: $secondAvg")

      // Adding a summary sentence
      if (firstAvg != 0.0 && secondAvg != 0.0) {
        val difference = (firstAvg - secondAvg).abs
        val summary = if (firstAvg > secondAvg) {
          s"Over the last 2 years, $firstFood is $difference more expensive than $secondFood."
        } else if (secondAvg > firstAvg) {
          s"Over the last 2 years, $secondFood is $difference more expensive than $firstFood."
        } else {
          s"Over the last 2 years, $firstFood and $secondFood have the same average price."
        }
        println(summary)
      }
    }
  }
}

object ShoppingManager {
  def goShopping(data: Map[String, List[Int]]): Unit = {
    val basket = scala.collection.mutable.Map[String, Float]()
    val currentPrices = PriceAnalyzer.getCurrentPrices(data)

    println("\nGo Shopping!")
    displayFoodListWithPrices(data.keys.toList, currentPrices)

    var shoppingOption = "continue"
    while (shoppingOption != "back" && shoppingOption != "exit") {
      shoppingOption = handleShopping(basket, data, currentPrices)
    }
  }

  private def handleShopping(basket: scala.collection.mutable.Map[String, Float], data: Map[String, List[Int]], currentPrices: Map[String, Int]): String = {
    println("\nEnter the number for the food item (or 'done' to finish):")
    val itemInput = readLine().trim

    if (itemInput.equalsIgnoreCase("done")) {
      calculateAndDisplayBasketTotal(basket.toMap, data)
      basketOptions(basket, data)
    } else {
      val itemNumber = Try(itemInput.toInt - 1).getOrElse(-1)
      data.keys.toList.lift(itemNumber) match {
        case Some(item) =>
          println(s"Enter the quantity for $item:")
          val quantityInput = readLine().trim
          val quantity = Try(quantityInput.toFloat).getOrElse(0f)
          if (quantity > 0) {
            basket.updateWith(item) {
              case Some(existingQuantity) => Some(existingQuantity + quantity)
              case None => Some(quantity)
            }
            calculateAndDisplayBasketTotal(basket.toMap, data) // Display running total
          } else {
            println("Invalid quantity. Please enter a positive number.")
          }
        case None => println("Invalid item number. Please enter a valid number.")
      }
      "continue"
    }
  }

  private def basketOptions(basket: scala.collection.mutable.Map[String, Float], data: Map[String, List[Int]]): String = {
    println("\nBasket Options:")
    println("1. Pay Now")
    println("2. Edit Basket")
    println("3. Clear Basket")
    println("4. Back")
    println("5. Exit")
    print("Enter your choice: ")

    readLine().trim match {
      case "1" =>
        println("\nYou're our 1 billionth user and done need to pay! Your order is on the way!")
        println("1. Main Menu")
        println("2. Exit")
        val postPaymentChoice = readLine().trim
        if (postPaymentChoice == "2") "exit" else "back"
      case "2" => "edit" // Implement the Edit Basket functionality
      case "3" =>
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

  private def displayFoodListWithPrices(foodItems: List[String], prices: Map[String, Int]): Unit = {
    println("\nAvailable Food Items with Current Prices:")
    foodItems.zipWithIndex.foreach { case (food, index) =>
      val price = prices.getOrElse(food, 0) / 100.0
      println(s"${index + 1}. $food - £$price")
    }
  }

  private def calculateAndDisplayBasketTotal(basket: Map[String, Float], data: Map[String, List[Int]]): Unit = {
    val currentPrices = PriceAnalyzer.getCurrentPrices(data)
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
