package uk.ac.city.toreador.rest.api.jpa.repositories;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.toreador.rest.api.entities.User;
import uk.ac.city.toreador.rest.api.entities.ValidationProject;

public interface ValidationProjectsRepository extends CrudRepository<ValidationProject, Long>{
	ValidationProject findById(Long id);
	Set<ValidationProject> findByUser(User user);
}
