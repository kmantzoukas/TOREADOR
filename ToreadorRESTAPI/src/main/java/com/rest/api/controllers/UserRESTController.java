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
	public ResponseEntity<?> getAllUsers() {
		
		List<User> users = null;
		
		try {
			users = (List<User>) repository.findAll();
			log.info("Fetching list of all users. Total number of users fetched is " + users.size());
			return new ResponseEntity<List<User>>(users,HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method returns a user with the specified id that is stored in the database
	 */
	@RequestMapping(value = "/rest/api/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		
		User user = null;
		
		try {
			user = repository.findById(id);
			log.info("Fetching " + user.toString());
			return new ResponseEntity<User>(user,HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
