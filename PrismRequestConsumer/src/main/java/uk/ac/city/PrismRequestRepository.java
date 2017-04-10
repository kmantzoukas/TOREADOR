package uk.ac.city;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.PrismRequest;

public interface PrismRequestRepository extends CrudRepository<PrismRequest, Long>{
	PrismRequest findById(Long id);
	List<PrismRequest> findByStatus(Status status);
}
