package uk.ac.city.toreador.rest.api.jpa.repositories;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.toreador.rest.api.entities.Project;

public interface ProjectsRepository extends CrudRepository<Project, Integer>{
	Set<Project> findByStatus(String status);
}
