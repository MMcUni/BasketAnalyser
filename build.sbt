name := "BasketAnalyser"

version := "0.1"

scalaVersion := "2.13.12" // Make sure this version matches your Scala version

libraryDependencies ++= Seq(
  // Add ScalaTest dependency for testing
  "org.scalatest" %% "scalatest" % "3.2.10" % Test
)

// Ensures that the test configurations are added to your project
Test / fork := true