import analysis.PriceAnalyzer
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}
import scala.io.StdIn.readLine

object Main extends App {
  DataManager.readData match {
    case Success(data) => runApp(data)
    case Failure(exception) => println(s"Error reading data: ${exception.getMessage}")
  }

  def runApp(data: Map[String, List[Int]]): Unit = {
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
      currentPrices.foreach { case (food, price) => println(s"$food: $price") }
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
    var continueShopping = true

    println("\nGo Shopping!")
    println("Enter your items in the format 'Item Quantity'. Enter 'done' when finished.")

    while (continueShopping) {
      val input = readLine("Enter item and quantity: ").trim
      if (input.equalsIgnoreCase("done")) {
        continueShopping = false

      } else {
        val parts = input.split("\\s+")
        if (parts.length == 2) {
          val item = parts(0).toUpperCase
          val quantity = Try(parts(1).toFloat).getOrElse(0f)
          if (quantity > 0) {
            basket.updateWith(item) {
              case Some(existingQuantity) => Some(existingQuantity + quantity)
              case None => Some(quantity)
            }
          } else {
            println("Invalid quantity. Please enter a positive number.")
          }
        } else {
          println("Invalid input. Please use the format 'Item Quantity'.")
        }
      }
    }
    calculateAndDisplayBasketTotal(basket.toMap, data)
  }

  /**
   * Calculates and displays the total value of the shopping basket.
   *
   * @param basket The user's shopping basket.
   */
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
    println(f"Total value of your basket: ${totalValue}%.2f")
  }
}
