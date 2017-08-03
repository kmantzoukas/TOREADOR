package uk.ac.city.toreador.rest.api.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.toreador.rest.api.entities.User;
import uk.ac.city.toreador.rest.api.entities.ValidationProject;

public interface ValidationProjectsRepository extends CrudRepository<ValidationProject, Long>{
	ValidationProject findById(Long id);
	List<ValidationProject> findByUser(User user);
}
