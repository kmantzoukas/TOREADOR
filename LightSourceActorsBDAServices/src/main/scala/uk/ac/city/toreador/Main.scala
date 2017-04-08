package uk.ac.city.toreador

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import uk.ac.city.toreador.actors.Worker

object Main {

  def main(args: Array[String]) {
	
    print("Type in the number of workers:")
    val numOfWorkers = readInt()
    
    print("Type in the lower bound:")
    var a = readInt()
    
    print("Type in the upper bound:")
    var b = readInt()
    
    val system = ActorSystem("system")
    val workers:Array[ActorRef] = new Array[ActorRef](numOfWorkers)
    
    val distance:Int = Math.abs(a-b) / numOfWorkers
    val mod:Int = Math.abs(a-b) % numOfWorkers
    
    for( i <- 0 to (workers.length - 1)){
    	workers(i) = system.actorOf(Props[Worker], name = "worker-" + i)
    	if(i == workers.length-1)
    		workers(i) ! new Range(a,b, 1)
    	else
    	  workers(i) ! new Range(a,a+distance-1, 1)
    	a = a + distance
      
    }
    system.shutdown
  }

}