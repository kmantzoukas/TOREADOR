package uk.ac.city.toreador.rest.api.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.toreador.rest.api.entities.Operation;

public interface OperationsRepository extends CrudRepository<Operation, Long> {

}
