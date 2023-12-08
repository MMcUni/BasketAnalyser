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

  "parseLine" should "correctly parse a valid line into a tuple" in {
    val line = "Apple, 120, 130, 140"
    val result = DataManager.parseLine(line)
    result._1 shouldBe "Apple"
    result._2 shouldBe List(120, 130, 140)
  }

  it should "throw an exception for an invalid line" in {
    val line = "Invalid Data"
    an[IllegalArgumentException] should be thrownBy DataManager.parseLine(line)
  }
}
