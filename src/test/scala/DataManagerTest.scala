import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.util.{Failure, Success}

class DataManagerTest extends AnyFlatSpec with Matchers {

  "readData" should "return a Success containing a Map for valid data" in {
    val result = DataManager.readData()
    result shouldBe a[Success[_]]
    result.get shouldBe a[Map[_, _]]
  }

  it should "return a Failure for invalid data" in {
    val originalFilename = DataManager.filename
    DataManager.filename = "invalid/path/data.txt"
    val result = DataManager.readData()
    result shouldBe a[Failure[_]]
    DataManager.filename = originalFilename // Reset the filename after test
  }

  it should "correctly parse a valid line from the data file" in {
    val line = "APPLE, 100, 150, 200"
    val result = DataManager.parseLine(line)
    result shouldBe Some("APPLE", List(100, 150, 200))
  }

  it should "return None for a line with invalid format" in {
    val invalidLine = "INVALID_LINE"
    DataManager.parseLine(invalidLine) shouldBe None
  }

  it should "return None for a line with non-numeric price values" in {
    val lineWithNonNumericValues = "APPLE, 100, abc, 200"
    DataManager.parseLine(lineWithNonNumericValues) shouldBe None
  }
}
