package uk.ac.city.services

import org.apache.spark.sql.SparkSession
import uk.ac.city.Model._
import uk.ac.city.listeners.SLAListener

object AverageConsumptionGroupByKey {
	
  val spark = SparkSession.builder.getOrCreate
  
  def main(args: Array[String]) {
    
    import spark.implicits._
    
    /*
     * Added an SLA listener on the SparkContext
     */
    spark.sparkContext.addSparkListener(new SLAListener)
    
    /*
     * Read all the measurements and place the in Dataset[Measurement]
     */
    val measurements = spark.read.json(args(0)).as[Measurement]
    
    /*
     * Flatten all values to combine the gateway id with the appliance type and the corresponding value of consumption
     */
    measurements.flatMap(_.explode)
    /*
     * Map the measurements Dataset into an RDD with key:(id,applianceType) and value:consumption
     */
    .map{
      case (id,applianceType,consumption) => ((id,applianceType),consumption) 
    }
    
    /*
     * Convert measurements into and RDD and group them by key which the combination of id and appliance type
     */
    .rdd
    
    /*
     * Group the measurements by (id,applianceType)
     */
    .groupByKey()
    
    /*
     * Perform a map operation to calculate the final result
     */
    .map{
      case ((id,applianceType),consumption) => ((id,applianceType),consumption.sum / consumption.size)
    }
    
    .collect.foreach(println)
    
    spark.stop
  }
  
}
