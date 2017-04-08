package uk.ac.city.lightsource.model;

import java.util.List;

public class Measurement {
	
	private int id;
	private Double energyProduced;
	private List<Appliance> appliances;
	private long timestamp;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Double getEnergyProduced() {
		return energyProduced;
	}
	public void setEnergyProduced(Double energyProduced) {
		this.energyProduced = energyProduced;
	}
	public List<Appliance> getAppliances() {
		return appliances;
	}
	public void setAppliances(List<Appliance> appliances) {
		this.appliances = appliances;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
