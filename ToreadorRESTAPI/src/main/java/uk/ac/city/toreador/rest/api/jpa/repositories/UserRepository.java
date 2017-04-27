package uk.ac.city.toreador.rest.api.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.toreador.rest.api.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{
	User findById(Long id);
}
