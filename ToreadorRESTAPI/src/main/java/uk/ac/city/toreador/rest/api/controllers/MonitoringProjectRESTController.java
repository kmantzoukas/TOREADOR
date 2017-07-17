package uk.ac.city.toreador.rest.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.city.toreador.rest.api.entities.MonitoringProject;
import uk.ac.city.toreador.rest.api.entities.MonitoringProjectStatus;
import uk.ac.city.toreador.rest.api.entities.User;
import uk.ac.city.toreador.rest.api.jpa.repositories.MonitoringProjectRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.UserRepository;

@Api(tags= "Monitoring project resource")
@RestController
public class MonitoringProjectRESTController{
	
	final static Logger log = Logger.getLogger(MonitoringProjectRESTController.class);
	
	@Autowired
	MonitoringProjectRepository monitoringProjectRepository;
	
	@Autowired
	UserRepository userRepository;
	
	/*
	 * This method returns a list of all the monitoring projects stored in the database
	 */
	@ApiOperation(value = "Return a list of all the monitoring projects in the database", nickname = "getAllMonitoringProjects")
	@ApiResponses({
	    @ApiResponse(code =  404, message ="Not found"),
	    @ApiResponse(code =  400, message ="Invalid input")
	})
	@RequestMapping(value = "/rest/api/projects/monitoring", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<MonitoringProject>> getAllMonitoringProjects() {
		
		List<MonitoringProject> projects = null;
		
		try {
			projects = (List<MonitoringProject>) monitoringProjectRepository.findAll();
			log.info("Fetching the list of all monitoring projects. Total number of monitoring projects fetched is " + projects.size());
			return new ResponseEntity<List<MonitoringProject>>(projects,HttpStatus.OK);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method returns the monitoring project with the specified id that is stored in the database
	 */
	@ApiOperation(
			value = "Get the monitoring project with the specified id from the database", 
			nickname = "getMonitoringProjectsByUser",
			response = MonitoringProject.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The user with the specified id does was not found in the databse"),
	    @ApiResponse(code =  400, message ="The user with the id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/users/{uid}/projects/monitoring", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<MonitoringProject>> getMonitoringProjectsByUser(@PathVariable Long uid) {
		
		try {
			User user = userRepository.findById(uid);
			
			if(user != null){
				List<MonitoringProject> projects = monitoringProjectRepository.findByUser(user);
				log.info("Fetching list of monitoring projects for user " + user.toString());
				return new ResponseEntity<List<MonitoringProject>>(projects,HttpStatus.OK);
			}else{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method returns the monitoring project with the specified id that is stored in the database
	 */
	@ApiOperation(
			value = "Get the monitoring project with the specified id from the database", 
			nickname = "getMonitoringProjectById",
			response = MonitoringProject.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The monitoring project with the specified id does was not found in the databse"),
	    @ApiResponse(code =  400, message ="The monitoring project with the id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/projects/monitoring/{pid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<MonitoringProject> getMonitoringProjectById(@PathVariable Long pid) {
		
		MonitoringProject monitoringProject = null;
		
		try {
			monitoringProject = monitoringProjectRepository.findById(pid);
			
			if(monitoringProject != null){
				log.info("Fetching monitoring project " + monitoringProject.toString());
				return new ResponseEntity<MonitoringProject>(monitoringProject,HttpStatus.OK);
			}else{
				log.info("Monitoring project with id:" + pid + " was not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method updates a monitoring project and stores it in the database
	 */
	@ApiOperation(
			value = "Update a monitoring project and store it in the database", 
			nickname = "putMonitoringProject",
			response = MonitoringProject.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The monitoring project with the specified id was not found in the databse"),
	    @ApiResponse(code =  400, message ="The monitoring project with the id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/projects/monitoring", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public ResponseEntity<MonitoringProject> putMonitoringProject(@RequestBody MonitoringProject project) {
		
		try {
			
			MonitoringProject p = monitoringProjectRepository.findById(project.getId());
			
			if(p != null){
				p = monitoringProjectRepository.save(project);
				log.info("Monitoring project updated successully " + p);
				return new ResponseEntity<MonitoringProject>(p, HttpStatus.CREATED);
			}else{
				log.info("Monitoring project with id: " + project.getId() + " was not found and therefore can not be updated");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method creates a new monitoring project and stores it in the database
	 */
	@ApiOperation(
			value = "Create a new monitoring project and store it in the database", 
			nickname = "createMonitoringProject",
			response = MonitoringProject.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The monitoring project with the specified id does was not found in the databse"),
	    @ApiResponse(code =  400, message ="The monitoring project with the id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/projects/monitoring", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<MonitoringProject> postMonitoringProject(@RequestBody MonitoringProject project) {
		
		try {
			project.setStatus(MonitoringProjectStatus.CREATED);
			MonitoringProject p = monitoringProjectRepository.save(project);
			log.info("Monitoring project saved successully " + p);
			return new ResponseEntity<MonitoringProject>(p,HttpStatus.OK);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method returns the monitoring project with the specified id that is stored in the database
	 */
	@ApiOperation(
			value = "Delete the monitoring project with the specified id from the database", 
			nickname = "deleteMonitoringProjectById",
			response = MonitoringProject.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The monitoring project with the specified id does was not found in the databse"),
	    @ApiResponse(code =  400, message ="The monitoring project with the id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/projects/monitoring/{pid}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<?> deleteMonitoringProjectById(@PathVariable Long pid) {
		
		try {
			MonitoringProject p = monitoringProjectRepository.findById(pid);
			
			if(p != null){
				monitoringProjectRepository.delete(p);
				log.info("Deleted monitoring project with id:" + pid);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else{
				log.info("Monitoring project with id:" + pid + " was not found and therefore can not be deleted");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
