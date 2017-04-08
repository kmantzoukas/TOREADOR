package uk.ac.city.lightsource.model;

public class Appliance {
	
	private TYPE applianceType;
	private Double consumption;
	
	public TYPE getApplianceType() {
		return applianceType;
	}
	public void setApplianceType(TYPE applianceType) {
		this.applianceType = applianceType;
	}
	public Double getConsumption() {
		return consumption;
	}
	public void setConsumption(Double consumption) {
		this.consumption = consumption;
	}
}
