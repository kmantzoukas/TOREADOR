package uk.ac.city.toreador.rest.api.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.toreador.rest.api.entities.PrismRequest;

public interface PrismRequestsRepository extends CrudRepository<PrismRequest, Long>{
	PrismRequest findById(Long id);
}
