import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MenuManagerTest extends AnyFlatSpec with Matchers {

  "MenuManager" should "display the correct main menu options" in {
    val outputStream = new ByteArrayOutputStream()
    Console.withOut(new PrintStream(outputStream)) {
      MenuManager.displayMainMenu()
    }

    val output = outputStream.toString
    output should include ("Main Menu:")
    output should include ("1. Price Analysis")
    output should include ("2. Go Shopping!")
    output should include ("3. Exit")
  }

  it should "display the correct price analysis menu options" in {
    val outputStream = new ByteArrayOutputStream()
    Console.withOut(new PrintStream(outputStream)) {
      MenuManager.displayPriceAnalysisMenu()
    }

    val output = outputStream.toString
    output should include ("Price Analysis Menu:")
    output should include ("1. Current Food Prices")
    output should include ("2. Price Range (min/max)")
    output should include ("3. Median Price in Period")
    output should include ("4. Largest Price Increase in 6 Months")
    output should include ("5. Compare 2-Year Prices")
    output should include ("6. Back")
  }
}
