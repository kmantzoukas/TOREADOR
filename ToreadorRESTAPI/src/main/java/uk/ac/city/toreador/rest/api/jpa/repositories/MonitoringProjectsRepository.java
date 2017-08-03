package uk.ac.city.toreador.rest.api.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import uk.ac.city.toreador.rest.api.entities.MonitoringProject;
import uk.ac.city.toreador.rest.api.entities.User;

public interface MonitoringProjectsRepository extends CrudRepository<MonitoringProject, Long>{
	MonitoringProject findById(Long id);
	List<MonitoringProject> findByUser(User user);
}
