package uk.ac.city.toreador.rest.api.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.toreador.rest.api.entities.User;

public interface UsersRepository extends CrudRepository<User, Integer>{
	User findById(Integer id);
}
