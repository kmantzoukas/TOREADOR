package uk.ac.city

object Model {
	/*
	 * Model representation as a case class of the an appliance
	 */
	final case class Appliance(applicanceType: String, consumption: Double)
	
	/*
	 * Model representation as a case class of a measurement
	 */
	final case class Measurement(id: String, energyProduced: Double, timestamp: Long, appliances: Seq[Appliance]){
	  def explode =  for(appliance <- appliances) yield (this.id, appliance.applicanceType, appliance.consumption)
	}
	
	/*
	 * Model representation as a case class of a gateway
	 */
	final case class Gateway(id: String, name: String, email: String, address: String, residents:Long)
}