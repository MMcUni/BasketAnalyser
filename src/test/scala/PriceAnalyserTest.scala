import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import analysis.PriceAnalyser

class PriceAnalyserTest extends AnyFlatSpec with Matchers {

  // Sample test data
  val testData = Map(
    "TOMATO" -> List(232, 188, 251, 316, 162, 168, 287, 174, 243, 299, 164, 279, 204, 159, 172, 261, 257, 167, 211, 173, 291, 258, 165, 294),
    "POTATO" -> List(65, 78, 130, 130, 124, 57, 123, 124, 100, 56, 78, 55, 121, 67, 87, 103, 68, 119, 65, 123, 89, 121, 137, 73)
    // Add more items as per your data.txt file
  )

  "PriceAnalyser" should "correctly calculate the current (most recent) price for each food" in {
    val currentPrices = PriceAnalyser.getCurrentPrices(testData)
    currentPrices should contain ("TOMATO" -> 294)
    currentPrices should contain ("POTATO" -> 73)
    // ... Additional assertions for other items
  }

  it should "calculate the highest and lowest prices within the period for each food" in {
    val minMaxPrices = PriceAnalyser.getMinMaxPrices(testData)
    minMaxPrices should contain ("TOMATO" -> (159, 316)) // Assuming 159 is the lowest and 316 is the highest for TOMATO
    minMaxPrices should contain ("POTATO" -> (55, 137))  // Similarly for POTATO
    // ... Additional assertions for other items
  }

  // ... Additional test cases for getMedianPrice, getLargestPriceIncrease, and getAveragePrice methods ...

}
