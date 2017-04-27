package com.rest.api.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.rest.api.entities.PrismRequest;

public interface PrismRequestRepository extends CrudRepository<PrismRequest, Long>{
	PrismRequest findById(Long id);
}
