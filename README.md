# BasketAnalyzer

## Project Description
BasketAnalyzer is a Scala-based application that offers insightful analysis of food basket prices. It processes data from a comma-separated text file (`data.txt`), which contains average monthly prices over two years for various basic food items. The application provides functionalities for viewing current prices, identifying price trends, and comparing average values of different food items. Additionally, it allows users to simulate shopping experiences and calculate the total value of a selected food basket.

## Features
- **Current Price Viewing:** Display the most recent prices of food items.
- **Price Trend Analysis:** Identify the highest and lowest prices for each item over a specified period.
- **Median Price Calculation:** Calculate the median price for each food item.
- **Price Rise Identification:** Determine the food item with the largest price increase over the last 6 months.
- **Average Price Comparison:** Compare average prices over 2 years for two selected food items.
- **Shopping Simulation:** Allow users to create a virtual food basket and calculate its total value based on current prices.
- **User-Friendly Interface:** A text-based menu-driven interface for easy navigation and interaction.

## To-Do List
- [x] Set up the Scala project environment.
- [x] Read and parse data from `data.txt` into `Map[String, List[Int]]`.
- [x] Implement functionality to get the current price for each food item.
- [x] Develop methods to find the highest and lowest prices for each item.
- [x] Create functionality for calculating the median price for each item.
- [x] Identify the item with the largest price rise over the last 6 months.
- [x] Implement average price comparison over 2 years for selected items.
- [x] Develop shopping basket feature to calculate total basket value.
- [x] Design a user-friendly text-based interface with a menu.
- [x] Write comprehensive unit tests for each analysis function.
- [x] Document the application, including test plans and results.
- [x] Write an evaluation report discussing functional thinking, programming style, and the comparison of functional and imperative styles (800-1000 words).
- [x] Finalize and clean up the code for submission.

## Installation and Usage
To set up and run BasketAnalyzer, follow these steps:

Ensure Scala and sbt (Scala Build Tool) are installed on your system.
Clone the BasketAnalyzer repository from GitHub or download the source code.
Navigate to the root directory of the project.
Run the application using the command: sbt run.
Follow the on-screen instructions to interact with the application.

## Contributing
If you are interested in contributing to BasketAnalyzer, please follow these guidelines:

Fork the GitHub repository.
Create a new branch for your feature or bug fix.
Write and test your code.
Submit a pull request with a detailed description of your changes.

## License
BasketAnalyzer is licensed under the MIT License. For more information, please refer to the LICENSE file in the project repository.
