package uk.ac.city.toreador.rest.api.entities;

import io.swagger.annotations.ApiModel;

@ApiModel
public enum Status {
	CREATED,PROCESSING,COMPLETED,ERROR
}
