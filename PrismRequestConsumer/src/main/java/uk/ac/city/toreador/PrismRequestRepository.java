package uk.ac.city.toreador;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.toreador.PrismRequest;
import uk.ac.city.toreador.entities.Status;

public interface PrismRequestRepository extends CrudRepository<PrismRequest, Long>{
	PrismRequest findById(Long id);
	List<PrismRequest> findByStatus(Status status);
}
