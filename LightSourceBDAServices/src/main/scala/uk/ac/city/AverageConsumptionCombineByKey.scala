package uk.ac.city

import org.apache.spark.sql.SparkSession
import uk.ac.city.Model._

object AverageConsumptionCombineByKey {
	
  val spark = SparkSession.builder.getOrCreate
  
  def main(args: Array[String]) {
    
    import spark.implicits._
    
    /*
     * Functions to be fed to the combiner
     */
    val createCombiner = (consumption:Double) => (consumption,1)
    val mergeValue = (acc:(Double, Int), consumption:Double) => (acc._1 + consumption, acc._2 + 1)
    val mergeCombiners = (acc1:(Double, Int), acc2:(Double, Int)) =>(acc1._1 + acc2._1, acc1._2 + acc2._2)
    
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
    
    .combineByKey(createCombiner,mergeValue,mergeCombiners)
    
    /*
     * Perform a map operation to calculate the final result
     */
    .map{
      case ((id,applianceType),(sum,count)) => ((id,applianceType),sum/count)
    }
    
    .collect.foreach(println)
    
    spark.stop
  }
  
}
