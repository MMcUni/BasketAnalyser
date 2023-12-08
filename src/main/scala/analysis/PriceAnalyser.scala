package analysis

object PriceAnalyser {
  def getCurrentPrices(pricesData: Map[String, List[Int]]): Map[String, Int] = {
    pricesData.map { case (food, prices) => (food, prices.last) }
  }

  def getMinMaxPrices(pricesData: Map[String, List[Int]]): Map[String, (Int, Int)] = {
    pricesData.map { case (food, prices) => (food, (prices.min, prices.max)) }
  }

  /**
   * Calculates the median price from a list of prices.
   *
   * @param prices List of prices for a food item.
   * @return The median price.
   */
  def getMedianPrice(prices: List[Int]): Int = {
    val sortedPrices = prices.sorted
    val middle = sortedPrices.length / 2
    if (sortedPrices.length % 2 == 0) {
      // For even number of elements, take average of middle two
      (sortedPrices(middle - 1) + sortedPrices(middle)) / 2
    } else {
      // For odd number of elements, take the middle one
      sortedPrices(middle)
    }
  }


  /**
   * Finds the food item with the largest price increase over the last 6 months.
   *
   * @param pricesData Map of food items with their monthly prices.
   * @return The food item with the largest price increase and the amount of increase.
   */
  def getLargestPriceIncrease(pricesData: Map[String, List[Int]]): (String, Int) = {
    pricesData.map { case (food, prices) =>
      val last6Months = prices.takeRight(6)
      val previous6Months = prices.dropRight(6).takeRight(6)
      val increase = last6Months.sum - previous6Months.sum
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
