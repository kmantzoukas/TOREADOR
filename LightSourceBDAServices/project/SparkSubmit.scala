import sbtsparksubmit.SparkSubmitPlugin.autoImport._

object SparkSubmit {
  lazy val settings = SparkSubmitSetting(
    SparkSubmitSetting("group-by-key-local",
      Seq(
          "--class", 
          "uk.ac.city.services.AverageConsumptionGroupByKey",
          "--master",
          "local[4]"),
      Seq("c:/measurements.json")
    ),
    SparkSubmitSetting("combine-by-key-local",
      Seq(
          "--class", 
          "uk.ac.city.services.AverageConsumptionCombineByKey",
          "--master",
          "local[4]"),
      Seq("c:/measurements.json")
    )
  )
}

