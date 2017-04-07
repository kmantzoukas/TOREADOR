package com.rest.api.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest.api.entities.PrismRequest;
import com.rest.api.jpa.repositories.PrismRequestRepository;

@RestController
public class PrismRequestRESTController{
	
	final static Logger log = Logger.getLogger(PrismRequestRESTController.class);
	
	@Autowired
	PrismRequestRepository repository;
	
	@RequestMapping("/rest/api/requests/prism")
	public ResponseEntity<?> getAllPatients() {
		
		List<PrismRequest> requests = null;
		
		try {
			requests = (List<PrismRequest>) repository.findAll();
			log.info("Fetching the list of all prism requests. Total number of prism requests fetched is " + requests.size());
			return new ResponseEntity<List<PrismRequest>>(requests,HttpStatus.OK);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping("/rest/api/requests/prism/{id}")
	public ResponseEntity<?> getPatientById(@PathVariable Long id) {
		
		PrismRequest request = null;
		
		try {
			request = repository.findById(id);
			log.info("Fetching prism request " + request.toString());
			return new ResponseEntity<PrismRequest>(request,HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
