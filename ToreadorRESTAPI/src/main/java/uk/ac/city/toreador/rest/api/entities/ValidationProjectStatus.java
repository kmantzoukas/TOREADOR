package uk.ac.city.toreador.rest.api.entities;

import io.swagger.annotations.ApiModel;

@ApiModel
public enum ValidationProjectStatus {
	CREATING,CREATED,PROCESSING,COMPLETED,ERROR
}
