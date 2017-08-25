package uk.ac.city.toreador.rest.api.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.toreador.rest.api.entities.CompositeService;

public interface CompositeServicesRepository extends CrudRepository<CompositeService, Integer>{
	
}
