import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import analysis.PriceAnalyser
import scala.collection.mutable

class ShoppingManagerTest extends AnyFlatSpec with Matchers {

  // Mock data for testing, mirroring your application's data structure
  val testData: Map[String, List[Int]] = Map(
    "TOMATO" -> List(294, 258, 291, 173, 211, 167, 257, 261, 172, 159, 204, 279, 164, 299, 243, 174, 287, 168, 162, 316, 251, 188, 232),
    "POTATO" -> List(73, 137, 121, 89, 123, 65, 119, 68, 103, 87, 67, 121, 55, 78, 56, 100, 124, 123, 57, 124, 130, 130, 78, 65)
    // Add more items based on your data.txt file
  )

  "ShoppingManager" should "calculate the total value of the basket correctly" in {
    val basket = mutable.Map("TOMATO" -> 2.0f, "POTATO" -> 3.0f)
    val pricesData = PriceAnalyser.getCurrentPrices(testData)

    val totalValue = basket.foldLeft(0f) { case (total, (item, quantity)) =>
      total + (pricesData.getOrElse(item, 0) * quantity)
    }

    totalValue shouldBe 659.0f  // Corrected expected value
  }

  it should "clear the basket correctly" in {
    val basket = mutable.Map("TOMATO" -> 2.0f, "POTATO" -> 1.0f)
    basket.clear()
    basket shouldBe empty
  }

  it should "handle shopping item selection correctly" in {
    val basket = mutable.Map[String, Float]()
    val currentPrices = PriceAnalyser.getCurrentPrices(testData)

    // Simulating adding "TOMATO" to the basket
    val item = "TOMATO"
    val quantity = 1.5f  // Simulating 1.5 kg of tomatoes
    basket.updateWith(item) {
      case Some(existingQuantity) => Some(existingQuantity + quantity)
      case None => Some(quantity)
    }

    basket should contain ("TOMATO" -> 1.5f)
  }

  it should "display the correct total value after adding items" in {
    val basket = mutable.Map[String, Float]("TOMATO" -> 1.5f)
    val currentPrices = PriceAnalyser.getCurrentPrices(testData)

    val totalValue = basket.foldLeft(0f) { case (total, (item, quantity)) =>
      total + (currentPrices.getOrElse(item, 0) * quantity)
    }

    totalValue shouldBe 348.0f  // Corrected expected value
  }
}
