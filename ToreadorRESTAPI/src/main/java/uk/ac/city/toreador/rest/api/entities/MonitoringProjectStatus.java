package uk.ac.city.toreador.rest.api.entities;

import io.swagger.annotations.ApiModel;

@ApiModel
public enum MonitoringProjectStatus {
	CREATED,STARTED,PAUSED,FINISHED,ERROR
}
