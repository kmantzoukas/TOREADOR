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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rest.api.entities.PrismRequest;
import com.rest.api.entities.User;
import com.rest.api.jpa.repositories.PrismRequestRepository;

@Api(tags= "Prism request resource")
@RestController
public class PrismRequestRESTController{
	
	final static Logger log = Logger.getLogger(PrismRequestRESTController.class);
	
	@Autowired
	PrismRequestRepository repository;
	
	/*
	 * This method returns a list of all the prism requests stored in the database
	 */
	@ApiOperation(value = "Return a list of all the prism requests in the database", nickname = "getAllPrismRequests")
	@ApiResponses({
	    @ApiResponse(code =  404, message ="Not found"),
	    @ApiResponse(code =  400, message ="Invalid input")
	})
	@RequestMapping(value = "/rest/api/requests/prism", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getAllPrismRequests() {
		
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
	
	/*
	 * This method creates a new prism request and stores it in the database
	 */
	@ApiOperation(
			value = "Create a new prism request and store it in the database", 
			nickname = "createPrismRequest",
			response = PrismRequest.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The user with the specified id does was not found in the databse"),
	    @ApiResponse(code =  400, message ="The user id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/requests/prism", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<PrismRequest> createPrismRequest(@RequestBody PrismRequest request) {
		
		try {
			
			PrismRequest req = repository.save(request);
			log.info("Request saved successully " + req);
			return new ResponseEntity<PrismRequest>(req,HttpStatus.OK);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method returns a prism request with the specified id that is stored in the database
	 */
	@ApiOperation(
			value = "Return the prism request with the specified id from the database", 
			nickname = "getPrismRequestById",
			response = PrismRequest.class)
	@ApiResponses({
	    @ApiResponse(code =  404, message ="The user with the specified id does was not found in the databse"),
	    @ApiResponse(code =  400, message ="The user id provided is not in a valid format")
	})
	@RequestMapping(value = "/rest/api/requests/prism/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getPrismRequestById(@PathVariable Long id) {
		
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
