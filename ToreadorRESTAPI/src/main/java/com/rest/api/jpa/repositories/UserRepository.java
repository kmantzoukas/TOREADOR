package com.rest.api.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.rest.api.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{
	User findById(Long id);
}
