package analysis

object PriceAnalyzer {
  def getCurrentPrices(pricesData: Map[String, List[Int]]): Map[String, Int] = {
    pricesData.map { case (food, prices) => (food, prices.last) }
  }

  def getMinMaxPrices(pricesData: Map[String, List[Int]]): Map[String, (Int, Int)] = {
    pricesData.map { case (food, prices) => (food, (prices.min, prices.max)) }
  }
}