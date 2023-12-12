package analysis

object PriceAnalyser {
  def getCurrentPrices(pricesData: Map[String, List[Int]]): Map[String, Int] = {
    pricesData.map { case (food, prices) => (food, prices.last) }
  }

  def getMinMaxPrices(pricesData: Map[String, List[Int]]): Map[String, (Int, Int)] = {
    pricesData.collect {
      case (food, prices) if prices.nonEmpty =>
        val (min, max) = prices.foldLeft((prices.head, prices.head)) { case ((min, max), p) =>
          (math.min(min, p), math.max(max, p))
        }
        (food, (min, max))
    }
  }

  def getMedianPrice(prices: List[Int]): Int = {
    if (prices.isEmpty) {
      throw new IllegalArgumentException("Price list cannot be empty")
    } else {
      val sortedPrices = prices.sorted
      val middle = sortedPrices.length / 2
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
   * @return The average price.
   */
  def getAveragePrice(prices: List[Int]): Double = {
    if (prices.isEmpty) 0.0 else prices.sum.toDouble / prices.size
  }
}
