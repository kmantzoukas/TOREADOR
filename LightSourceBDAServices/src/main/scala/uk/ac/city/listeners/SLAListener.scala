package uk.ac.city.listeners

import org.apache.log4j.Logger
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.scheduler.SparkListener
import org.apache.spark.scheduler.SparkListenerTaskEnd
import org.apache.spark.scheduler.SparkListenerTaskStart
import java.sql.DriverManager
import java.sql.Connection
import org.apache.spark.scheduler.SparkListenerJobEnd
import org.apache.spark.scheduler.SparkListenerJobStart
import org.apache.spark.scheduler.SparkListenerBlockUpdated
import org.apache.spark.scheduler.SparkListenerTaskGettingResult
import org.apache.spark.scheduler.SparkListenerUnpersistRDD
import org.apache.spark.scheduler.SparkListenerEvent
import org.apache.spark.scheduler.SparkListenerBus
import org.apache.spark.scheduler.SparkListenerBus

class SLAListener extends SparkListener{

  lazy val logger = Logger.getLogger(this.getClass())
  
  
  override def onOtherEvent(event:SparkListenerEvent){
    logger.info(s" ---------------> ${event.getClass()}")
  }
  
  override def onJobEnd(jobEnd:SparkListenerJobEnd) {
    logger.info(s"onJobEnd --> jobId: ${jobEnd.jobId} and jobResult: ${jobEnd.jobResult}")
  }
  
  override def onJobStart(jobStart:SparkListenerJobStart) {
    logger.info(s"${jobStart.jobId} - ${jobStart.properties} - ${jobStart.stageIds} - ${jobStart.stageInfos}")  
  }
  
  override def onBlockUpdated(blockUpdated:SparkListenerBlockUpdated){
    logger.info(s" -------> ${blockUpdated.blockUpdatedInfo}")
  }
  
  override def onTaskGettingResult(taskGettingResult:SparkListenerTaskGettingResult){
    logger.info(s"=======> ${taskGettingResult.taskInfo.accumulables}")
  }
  
  override def onUnpersistRDD(unpersistRDD:SparkListenerUnpersistRDD){
    logger.info(s"Unpersit RDD: ${unpersistRDD.rddId}")
  }
  
  override def onTaskStart(taskStart:SparkListenerTaskStart){
    val message:String = s"TOREADOR ---> Happens(T_${taskStart.taskInfo.taskId}"
    println(message)
	//logger.info(message)  
  }
   
   override def onTaskEnd(taskEnd:SparkListenerTaskEnd){
	logger.info("Happens")
	val index:Int = taskEnd.taskInfo.index
	val id:Long = taskEnd.taskInfo.taskId
	val recordsWritten:Long = taskEnd.taskMetrics.outputMetrics.recordsWritten 
    println("Task with index $index%d and id $id%d wrote $recordsWritten%d records.")
  }
  
}