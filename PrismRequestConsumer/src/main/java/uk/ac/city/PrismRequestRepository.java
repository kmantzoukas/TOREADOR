package uk.ac.city;

import org.springframework.data.repository.CrudRepository;

public interface PrismRequestRepository extends CrudRepository<PrismRequest, Long>{
	PrismRequest findById(Long id);
}
