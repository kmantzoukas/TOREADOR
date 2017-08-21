package uk.ac.city.services

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.SparkSession

import org.apache.spark.rdd.RDD

import uk.ac.city.Model._
import uk.ac.city.listeners.SLAListener

object AverageConsumptionGroupByKey {

  val spark = SparkSession.builder.getOrCreate
  import spark.implicits._

  def main(args: Array[String]) {

    /*
     * Added an SLA listener on the SparkContext
     */
    spark.sparkContext.addSparkListener(new SLAListener)

    val path: String = args(0)
    
    printResults(
        collectResults(
            calculateAverages(
                groupByKey(
                    convertToRDD(
                        createKeyValuePairs(
                            explode(
                                load(path))))))))

    spark.stop
  }

  /*
   * Load the data from a data source. In our case from a JSON file
   */
  def load(path: String): Dataset[Measurement] = {
    spark.read.json(path).as[Measurement]
  }

  /*
   * Explode the data for the appliances for each measurement
   */
  def explode(measurements: Dataset[Measurement]): Dataset[(String, String, Double)] = {
    measurements.flatMap(_.explode)
  }

  /*
   * Create the (Key,Value) pairs where the key is the combination of the gateway id and the appliance type and the value is the
   * energy consumption. An example would look like this: (10,STOVE), 382.38)
   */
  def createKeyValuePairs(dataset: Dataset[(String, String, Double)]): Dataset[((String, String), Double)] = {
    dataset.map {
      case (id, applianceType, consumption) => ((id, applianceType), consumption)
    }
  }
  
  /*
   * Convert the data set into and RDD on which we can apply Apache Spark's API functions such as groupByKey, CombineByKey, etc.
   */
  def convertToRDD(dataset: Dataset[((String, String), Double)]):RDD[((String, String), Double)] = {
    dataset.rdd
  }
  /*
   * Group the items of the RDD by key
   */
  def groupByKey(dataset:RDD[((String, String), Double)]):RDD[((String, String), Iterable[Double])] = {
    dataset.groupByKey
  }
  
  /*
   * Calculate the averages for each key by adding the values and dividing them with the size of the  array that holds the respective values
   */
  def calculateAverages(dataset:RDD[((String, String), Iterable[Double])]):RDD[((String, String), Double)] = {
    dataset.map {
        case ((id, applianceType), consumption) => ((id, applianceType), consumption.sum / consumption.size)
      }
  }
  
  /*
   * Collect the array of results. The results will be represented as an array of <Key,Value> pairs
   * where the key is the combination of the gateway id and the appliance and the value is 
   * the average energy consumption
   */
  def collectResults(dataset:RDD[((String, String), Double)]):Array[((String, String), Double)] = {
   dataset.collect
  }
  
  /*
   * Print the results on the screen
   */
  def printResults(results:Array[((String, String), Double)]) = {
   results.foreach(println)
  }

}