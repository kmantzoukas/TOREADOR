package com.rest.api.controllers;

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


@RestController
public class UserRESTController {
	
	final static Logger log = Logger.getLogger(UserRESTController.class);
	
	@Autowired
	UserRepository repository;
	
	
	/*
	 * This method returns a list of all the users stored in the database
	 */
	@RequestMapping(value= "/rest/api/users", method = RequestMethod.GET)
	public ResponseEntity<?> getAllPatients() {
		
		/*
		 * Initialise the list of users to NULL
		 */
		List<User> users = null;
		
		try {
			/*
			 * Fetch the list of all the users from the database
			 */
			users = (List<User>) repository.findAll();
			/*
			 * Log the fetching action
			 */
			log.info("Fetching list of all users. Total number of users fetched is " + users.size());
			/*
			 * Return the list of users as part of the HTTP response
			 */
			return new ResponseEntity<List<User>>(users,HttpStatus.OK);
		} catch (Exception e) {
			/*
			 * Log the error as it happens
			 */
			log.error(e.getMessage());
			/*
			 * Return a 500 HTTP internal server error to the caller
			 */
			return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method returns a user with the specified id that is stored in the database
	 */
	@RequestMapping(value = "/rest/api/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientById(@PathVariable Long id) {
		
		/*
		 * Initialise the user to NULL
		 */
		User user = null;
		
		try {
			/*
			 * Fetch the user with the specified id from the database
			 */
			user = repository.findById(id);
			/*
			 * Log the fetching action
			 */
			log.info("Fetching " + user.toString());
			/*
			 * Return user as part of the HTTP response
			 */
			return new ResponseEntity<User>(user,HttpStatus.OK);
		} catch (Exception e) {
			/*
			 * Log the error as it happens
			 */
			log.error(e.getMessage());
			/*
			 * Return a 500 HTTP internal server error to the caller
			 */
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
