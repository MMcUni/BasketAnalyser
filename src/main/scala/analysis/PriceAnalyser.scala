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
