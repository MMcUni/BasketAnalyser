import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import analysis.PriceAnalyser

class PriceAnalyserTest extends AnyFlatSpec with Matchers {

  val testData = Map(
    "TOMATO" -> List(232, 188, 251, 316, 162, 168, 287, 174, 243, 299, 164, 279, 204, 159, 172, 261, 257, 167, 211, 173, 291, 258, 165, 294),
    "POTATO" -> List(65, 78, 130, 130, 124, 57, 123, 124, 100, 56, 78, 55, 121, 67, 87, 103, 68, 119, 65, 123, 89, 121, 137, 73),
    "RICE" -> List(46, 77, 76, 52, 63, 46, 75, 44, 76, 43, 79, 53, 71, 74, 67, 60, 69, 77, 69, 63, 59, 55, 51, 55),
    "BUTTER" -> List(670, 797, 726, 784, 776, 737, 764, 723, 668, 680, 781, 757, 692, 737, 688, 775, 757, 660, 669, 792, 796, 730, 737, 739),
    "FLOUR" -> List(59, 55, 58, 65, 54, 42, 66, 42, 48, 55, 62, 61, 42, 41, 63, 62, 49, 60, 58, 61, 66, 54, 49, 62),
    "OIL" -> List(2195, 2484, 2155, 1823, 2272, 2163, 1972, 2425, 1919, 2305, 1860, 2023, 2094, 1932, 2053, 2207, 2200, 2308, 1882, 1970, 2259, 2211, 2362, 2084),
    "CHICKEN" -> List(670, 820, 881, 742, 812, 783, 794, 718, 677, 642, 690, 677, 718, 719, 606, 848, 693, 734, 744, 602, 674, 814, 873, 789),
    "BEEF" -> List(1212, 1189, 1063, 964, 1253, 1163, 1216, 1235, 1246, 1278, 927, 1133, 1299, 1248, 1186, 1100, 1103, 1104, 1101, 953, 1146, 1224, 1105, 931),
    "APPLE" -> List(224, 208, 226, 256, 220, 214, 243, 276, 206, 213, 200, 272, 219, 268, 212, 246, 278, 203, 209, 226, 278, 248, 219, 281),
    "MILK" -> List(86, 92, 108, 93, 100, 77, 77, 101, 99, 100, 100, 89, 75, 79, 76, 117, 91, 117, 86, 100, 114, 80, 103, 71)
  )

  // Example of an independent calculation
  val manuallyCalculatedTomatoMedian = 221 // Manually calculated median for TOMATO

  "PriceAnalyser" should "correctly calculate the current (most recent) price for each food" in {
    val currentPrices = PriceAnalyser.getCurrentPrices(testData)
    currentPrices should contain("TOMATO" -> 294)
    currentPrices should contain("POTATO" -> 73)
    currentPrices should contain("RICE" -> 55)
    currentPrices should contain("BUTTER" -> 739)
    currentPrices should contain("FLOUR" -> 62)
    currentPrices should contain("OIL" -> 2084)
    currentPrices should contain("CHICKEN" -> 789)
    currentPrices should contain("BEEF" -> 931)
    currentPrices should contain("APPLE" -> 281)
    currentPrices should contain("MILK" -> 71)
  }

  it should "calculate the highest and lowest prices within the period for each food" in {
    val minMaxPrices = PriceAnalyser.getMinMaxPrices(testData)
    minMaxPrices should contain("TOMATO" -> (159, 316))
    minMaxPrices should contain("POTATO" -> (55, 137))
    minMaxPrices should contain("RICE" -> (43, 79))
    minMaxPrices should contain("BUTTER" -> (660, 797))
    minMaxPrices should contain("FLOUR" -> (41, 66))
    minMaxPrices should contain("OIL" -> (1823, 2484))
    minMaxPrices should contain("CHICKEN" -> (602, 881))
    minMaxPrices should contain("BEEF" -> (927, 1299))
    minMaxPrices should contain("APPLE" -> (200, 281))
    minMaxPrices should contain("MILK" -> (71, 117))
  }

  it should "correctly calculate the median price for each food" in {
    val tomatoMedian = PriceAnalyser.getMedianPrice(testData("TOMATO"))
    tomatoMedian shouldBe manuallyCalculatedTomatoMedian // Compare with independently calculated median
    PriceAnalyser.getMedianPrice(testData("TOMATO")) shouldBe 221
    PriceAnalyser.getMedianPrice(testData("POTATO")) shouldBe 94
    PriceAnalyser.getMedianPrice(testData("RICE")) shouldBe 63
    PriceAnalyser.getMedianPrice(testData("BUTTER")) shouldBe 737
    PriceAnalyser.getMedianPrice(testData("FLOUR")) shouldBe 58
    PriceAnalyser.getMedianPrice(testData("OIL")) shouldBe 2159
    PriceAnalyser.getMedianPrice(testData("CHICKEN")) shouldBe 726
    PriceAnalyser.getMedianPrice(testData("BEEF")) shouldBe 1154
    PriceAnalyser.getMedianPrice(testData("APPLE")) shouldBe 225
    PriceAnalyser.getMedianPrice(testData("MILK")) shouldBe 92
  }

  it should "identify the food with the largest price increase over the last 6 months" in {
    PriceAnalyser.getLargestPriceIncrease(testData) shouldBe("CHICKEN", 178) // Manually calculated price increase
  }

  it should "correctly calculate the average price over a 2-year period for each food and compare them" in {
    // Calculating and comparing the average prices
    PriceAnalyser.getAveragePrice(testData("APPLE")) shouldBe 235.20833333333334
    PriceAnalyser.getAveragePrice(testData("BUTTER")) shouldBe 734.7916666666666
    PriceAnalyser.getAveragePrice(testData("POTATO")) shouldBe 95.54166666666667
    PriceAnalyser.getAveragePrice(testData("FLOUR")) shouldBe 55.583333333333336
    PriceAnalyser.getAveragePrice(testData("RICE")) shouldBe 62.5
    PriceAnalyser.getAveragePrice(testData("MILK")) shouldBe 92.95833333333333
    PriceAnalyser.getAveragePrice(testData("TOMATO")) shouldBe 223.95833333333334
    PriceAnalyser.getAveragePrice(testData("OIL")) shouldBe 2131.5833333333335
    PriceAnalyser.getAveragePrice(testData("BEEF")) shouldBe 1140.7916666666667
    PriceAnalyser.getAveragePrice(testData("CHICKEN")) shouldBe  738.3333333333334

    // Additional assertions for comparisons
    (PriceAnalyser.getAveragePrice(testData("BUTTER")) - PriceAnalyser.getAveragePrice(testData("APPLE"))) shouldBe 499.58333333333326 // £5.00
    (PriceAnalyser.getAveragePrice(testData("POTATO")) - PriceAnalyser.getAveragePrice(testData("FLOUR"))) shouldBe 39.958333333333336   // £0.40
    (PriceAnalyser.getAveragePrice(testData("MILK")) - PriceAnalyser.getAveragePrice(testData("RICE"))) shouldBe 30.45833333333333     // £0.30
    (PriceAnalyser.getAveragePrice(testData("OIL")) - PriceAnalyser.getAveragePrice(testData("TOMATO"))) shouldBe 1907.6250000000002  // £19.08
    (PriceAnalyser.getAveragePrice(testData("BEEF")) - PriceAnalyser.getAveragePrice(testData("CHICKEN"))) shouldBe 402.45833333333337  // £4.02
  }
}