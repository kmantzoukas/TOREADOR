package uk.ac.city.toreador.actors

import akka.actor.Actor

class Worker extends Actor {
  
  def receive = {
    case r:Range => {
      var sum:Int = 0
      r.foreach(item => sum+= item)
      println("[%d,%d], sum:%d".format(r.start, r.end, sum))
    }
  }

}