package analysis

object PriceAnalyser {

  /**
   * Retrieves the most recent price for each food item.
   *
   * @param pricesData A map of food items to their historical prices.
   * @return A map of food items to their most recent price.
   */
  def getCurrentPrices(pricesData: Map[String, List[Int]]): Map[String, Int] = {
    pricesData.map { case (food, prices) => (food, prices.last) }
  }

  /**
   * Determines the minimum and maximum prices for each food item.
   *
   * @param pricesData A map of food items to their historical prices.
   * @return A map of food items to a tuple containing their minimum and maximum prices.
   */
  def getMinMaxPrices(pricesData: Map[String, List[Int]]): Map[String, (Int, Int)] = {
    pricesData.collect {
      case (food, prices) if prices.nonEmpty =>
        // Using foldLeft to find min and max prices in one pass through the list
        val (min, max) = prices.foldLeft((prices.head, prices.head)) { case ((min, max), p) =>
          (math.min(min, p), math.max(max, p))
        }
        (food, (min, max))
    }
  }

  /**
   * Calculates the median price of a list of prices.
   *
   * @param prices A list of prices for a particular food item.
   * @return The median price.
   * @throws IllegalArgumentException if the prices list is empty.
   */
  def getMedianPrice(prices: List[Int]): Int = {
    if (prices.isEmpty) {
      throw new IllegalArgumentException("Price list cannot be empty")
    } else {
      val sortedPrices = prices.sorted
      val middle = sortedPrices.length / 2
      // Handle even and odd number of prices
      if (sortedPrices.length % 2 == 0) {
        (sortedPrices(middle - 1) + sortedPrices(middle)) / 2
      } else {
        sortedPrices(middle)
      }
    }
  }

  /**
   * Finds the food item with the largest price increase over the last 6 months.
   *
   * This method iterates through the prices only once, calculating the sum of the last 6 months
   * and the previous 6 months simultaneously for efficiency.
   *
   * @param pricesData Map of food items with their monthly prices.
   * @return The food item with the largest price increase and the amount of increase.
   */
  def getLargestPriceIncrease(pricesData: Map[String, List[Int]]): (String, Int) = {
    pricesData.map { case (food, prices) =>
      val (last6MonthsSum, prev6MonthsSum) = prices.reverse.splitAt(6) match {
        case (last6Months, previousMonths) =>
          (last6Months.sum, previousMonths.take(6).sum)
      }
      val increase = last6MonthsSum - prev6MonthsSum
      (food, increase)
    }.maxBy(_._2)
  }

  /**
   * Calculates the average price of a food item over a 2-year period.
   *
   * @param prices List of monthly prices for a food item.
   * @return The average price, or 0.0 if the list is empty.
   */
  def getAveragePrice(prices: List[Int]): Double = {
    if (prices.isEmpty) 0.0 else prices.sum.toDouble / prices.size
  }
}