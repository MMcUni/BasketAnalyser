import scala.io.Source
import scala.util.{Try, Using, Success, Failure}

object Main extends App {
  // Define the file path for the data file
  private val filename = "src/main/data.txt"

  // Read the file and parse each line into a map of food items and their prices
  private val data: Try[Map[String, List[Int]]] = Using(Source.fromFile(filename)) { source =>
    source.getLines().map(parseLine).toMap
  }

  /**
   * Parses a line from the data file.
   *
   * Each line contains a food item followed by a series of comma-separated prices.
   * This function splits the line into parts, with the first part being the food item
   * and the rest being the prices. It returns a tuple of the food item and a list of its prices.
   *
   * @param line The line from the data file.
   * @return A tuple containing the food item and a list of prices.
   */
  private def parseLine(line: String): (String, List[Int]) = {
    val parts = line.split(", ")
    // Extract the food item name and the list of prices
    (parts.head, parts.tail.toList.map(_.toInt))
  }

  // Process the data or handle the error
  data match {
    case Success(parsedData) =>
      // Print a small sample of the data for verification
      println("Sample data:")
      parsedData.take(3).foreach { case (food, prices) =>
        println(s"$food: ${prices.mkString(", ")}")
      }

    case Failure(exception) =>
      println(s"An error occurred while reading the file: ${exception.getMessage}")
  }
}
