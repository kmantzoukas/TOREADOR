package uk.ac.city.toreador.rest.api.jpa.repositories;

import java.util.List;

import javax.persistence.OrderBy;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.toreador.rest.api.entities.Project;
import uk.ac.city.toreador.rest.api.entities.User;

public interface ProjectsRepository extends CrudRepository<Project, Integer>{
	Project findById(Integer id);
	@OrderBy("created desc")
	List<Project> findByUserOrderByCreatedDesc(User user);
}
