package com.rest.api.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rest.api.entities.User;
import com.rest.api.jpa.repositories.UserRepository;

@Api(tags= "User resource")
@RestController
public class UserRESTController {
	
	final static Logger log = Logger.getLogger(UserRESTController.class);
	
	@Autowired
	UserRepository repository;
	
	
	/*
	 * This method returns a list of all the users stored in the database
	 */
	@ApiOperation(value = "Return a list of all the users in the database", nickname = "getAllUsers")
	@RequestMapping(value= "/rest/api/users", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<User>> getAllUsers() {
		
		List<User> users = null;
		
		try {
			users = (List<User>) repository.findAll();
			log.info("Fetched all users. Total number of users fetched is " + users.size());
			return new ResponseEntity<List<User>>(users,HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method returns a user with the specified id that is stored in the database
	 */
	@ApiOperation(
			value = "Return the user with the specified id from the database", 
			nickname = "getUserById",
			response = User.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The user with the specified id was not found in the databse"),
	    @ApiResponse(code =  400, message ="The user id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/users/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		
		User user = null;
		
		try {
			user = repository.findById(id);
			
			if(user == null){
				log.info("User with id " + id + " was not found in the database");
				return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
			}
				
			else{
				log.info("Fetching " + user.toString());
				return new ResponseEntity<User>(user, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<User>(user, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
