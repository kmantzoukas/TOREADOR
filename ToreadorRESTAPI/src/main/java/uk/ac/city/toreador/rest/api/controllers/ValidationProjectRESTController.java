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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import uk.ac.city.toreador.rest.api.entities.User;
import uk.ac.city.toreador.rest.api.entities.ValidationProject;
import uk.ac.city.toreador.rest.api.entities.ValidationProjectStatus;
import uk.ac.city.toreador.rest.api.jpa.repositories.UserRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.ValidationProjectRepository;

@Api(tags= "Validation project resource")
@RestController
public class ValidationProjectRESTController{
	
	final static Logger log = Logger.getLogger(ValidationProjectRESTController.class);
	
	@Autowired
	ValidationProjectRepository validationProjectRepository;
	
	@Autowired
	UserRepository userRepository;
	
	/*
	 * This method returns a list of all the validation projects stored in the database
	 */
	@ApiOperation(value = "Return a list of all the validation projects in the database", nickname = "getAllValidationProjects")
	@ApiResponses({
	    @ApiResponse(code =  404, message ="Not found"),
	    @ApiResponse(code =  400, message ="Invalid input")
	})
	@RequestMapping(value = "/rest/api/projects/validation", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<ValidationProject>> getAllValidationProjects() {
		
		List<ValidationProject> requests = null;
		
		try {
			requests = (List<ValidationProject>) validationProjectRepository.findAll();
			log.info("Fetching the list of all validation projects. Total number of validation projects fetched is " + requests.size());
			return new ResponseEntity<List<ValidationProject>>(requests,HttpStatus.OK);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method returns the validation project with the specified id that is stored in the database
	 */
	@ApiOperation(
			value = "Get the validation project with the specified id from the database", 
			nickname = "getValidationProjectById",
			response = ValidationProject.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The user with the specified id was not found in the databse"),
	    @ApiResponse(code =  400, message ="The user with the id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/users/{uid}/projects/validation", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<ValidationProject>> getValidationProjectsByUser(@PathVariable Long uid) {
		
		try {
			User user = userRepository.findById(uid);
			
			if(user != null){
				List<ValidationProject> projects = validationProjectRepository.findByUser(user);
				log.info("Fetching list of validation projects for user " + user.toString());
				return new ResponseEntity<List<ValidationProject>>(projects,HttpStatus.OK);
			}else{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method returns the validation project with the specified id that is stored in the database
	 */
	@ApiOperation(
			value = "Get the validation project with the specified id from the database", 
			nickname = "getValidationProjectById",
			response = ValidationProject.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The validation project with the specified id was not found in the databse"),
	    @ApiResponse(code =  400, message ="The validation project with the id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/projects/validation/{pid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ValidationProject> getValidationProjectById(@PathVariable Long pid) {
		
		ValidationProject validationProject = null;
		
		try {
			validationProject = validationProjectRepository.findById(pid);
			
			if(validationProject != null){
				log.info("Fetching validation project " + validationProject.toString());
				return new ResponseEntity<ValidationProject>(validationProject,HttpStatus.OK);
			}else{
				log.info("Validation project with id:" + pid + " was not found");
				return new ResponseEntity<ValidationProject>(validationProject,HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method creates a new validation project and stores it in the database
	 */
	@ApiOperation(
			value = "Create a new validation project and store it in the database", 
			nickname = "createValidationProject",
			response = ValidationProject.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The validation project with the specified id was not found in the databse"),
	    @ApiResponse(code =  400, message ="The validation project with the id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/projects/validation", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<ValidationProject> postValidationProject(@RequestBody ValidationProject project) {
		
		try {
			project.setStatus(ValidationProjectStatus.CREATED);
			ValidationProject p = validationProjectRepository.save(project);
			log.info("Validation project saved successully " + p);
			return new ResponseEntity<ValidationProject>(p,HttpStatus.OK);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method updates a validation project and stores it in the database
	 */
	@ApiOperation(
			value = "Update a validation project and store it in the database", 
			nickname = "putValidationProject",
			response = ValidationProject.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The validation project with the specified id was not found in the databse"),
	    @ApiResponse(code =  400, message ="The validation project with the id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/projects/validation", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public ResponseEntity<ValidationProject> putValidationProject(@RequestBody ValidationProject project) {
		
		try {
			
			ValidationProject p = validationProjectRepository.findById(project.getId());
			
			if(p != null){
				p = validationProjectRepository.save(project);
				log.info("Validation project updated successully " + p);
				return new ResponseEntity<ValidationProject>(p, HttpStatus.CREATED);
			}else{
				log.info("Validation project with id: " + project.getId() + " was not found and therefore can not be updated");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method creates a new validation project and stores it in the database
	 */
	@ApiOperation(
			value = "Create a new model and associate it with a validation project and store it in the database", 
			nickname = "createModelForValidationProject",
			response = ValidationProject.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The validation project with the specified id was not found in the databse"),
	    @ApiResponse(code =  400, message ="The validation project with the id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/projects/validation/{pid}/model", method = RequestMethod.POST, consumes = "multipart/form-data",headers = {"Accept: application/pdf"})
	public ResponseEntity<ValidationProject> postModelForValidationProject(@PathVariable Long pid, @RequestParam("model") MultipartFile model) {
		
		try {
			
			ValidationProject project = validationProjectRepository.findById(pid);
			project.setModel(model.getBytes());
			validationProjectRepository.save(project);
			validationProjectRepository.findById(pid);
			log.info("Model for the validation project with id " + project.getId() + " has been saved successully");
			return new ResponseEntity<ValidationProject>(HttpStatus.OK);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method returns the validation project with the specified id that is stored in the database
	 */
	@ApiOperation(
			value = "Delete the validation project with the specified id from the database", 
			nickname = "deleteValidationProjectById",
			response = ValidationProject.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The validation project with the specified id was not found in the databse"),
	    @ApiResponse(code =  400, message ="The validation project with the id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/projects/validation/{pid}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<?> deleteValidationProjectById(@PathVariable Long pid) {
		
		try {
			ValidationProject p = validationProjectRepository.findById(pid);
			
			if(p != null){
				validationProjectRepository.delete(p);
				log.info("Deleted validation project with id:" + pid);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else{
				log.info("Validation project with id:" + pid + " was not found and therefore can not be deleted");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
