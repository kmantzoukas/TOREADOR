package uk.ac.city.toreador.rest.api.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQStaticContext;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import uk.ac.city.toreador.rest.api.entities.AssetType;
import uk.ac.city.toreador.rest.api.entities.AtomicService;
import uk.ac.city.toreador.rest.api.entities.CompositeService;
import uk.ac.city.toreador.rest.api.entities.GuardedAction;
import uk.ac.city.toreador.rest.api.entities.Input;
import uk.ac.city.toreador.rest.api.entities.Operation;
import uk.ac.city.toreador.rest.api.entities.Output;
import uk.ac.city.toreador.rest.api.entities.User;
import uk.ac.city.toreador.rest.api.entities.ValidationAsset;
import uk.ac.city.toreador.rest.api.entities.ValidationProject;
import uk.ac.city.toreador.rest.api.entities.ValidationProjectStatus;
import uk.ac.city.toreador.rest.api.jpa.repositories.AtomicServicesRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.CompositeServicesRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.GuardedActionsRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.InputsRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.OperationsRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.OutputsRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.UsersRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.ValidationAssetsRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.ValidationProjectsRepository;
import uk.ac.city.toreador.validation.Translate;

@Api(tags = "Validation project resource")
@RestController
public class ValidationProjectRESTController {

	final static Logger log = Logger.getLogger(ValidationProjectRESTController.class);

	@Autowired
	ValidationProjectsRepository validationProjectRepository;

	@Autowired
	UsersRepository userRepository;
	
	@Autowired
	CompositeServicesRepository compositeServiceRepository;
	
	@Autowired
	AtomicServicesRepository atomicServicesRepository;
	
	@Autowired
	ValidationAssetsRepository validationAssetsRepository;
	
	@Autowired
	OperationsRepository operationsRepository;

	@Autowired
	OutputsRepository outputsRepository;
	
	@Autowired
	InputsRepository inputsRepository;
	
	@Autowired
	GuardedActionsRepository guardedActionsRepository;
	

	/*
	 * This method returns a list of all the validation projects stored in the
	 * database
	 */
	@ApiOperation(value = "Return a list of all the validation projects in the database", nickname = "getAllValidationProjects")
	@ApiResponses({ @ApiResponse(code = 404, message = "Not found"),
			@ApiResponse(code = 400, message = "Invalid input") })
	@RequestMapping(value = "/rest/api/projects/validation", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Set<ValidationProject>> getAllValidationProjects() {

		Set<ValidationProject> requests = null;

		try {
			requests = (Set<ValidationProject>) validationProjectRepository.findAll();
			log.info("Fetching the list of all validation projects. Total number of validation projects fetched is "
					+ requests.size());
			return new ResponseEntity<Set<ValidationProject>>(requests, HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<Set<ValidationProject>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method returns the validation project with the specified id that is
	 * stored in the database
	 */
	@ApiOperation(value = "Get the validation project with the specified id from the database", nickname = "getValidationProjectById", response = ValidationProject.class)
	@ApiResponses({ @ApiResponse(code = 404, message = "The user with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The user with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/users/{uid}/projects/validation", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Set<ValidationProject>> getValidationProjectsByUser(@PathVariable Long uid) {

		try {
			User user = userRepository.findById(uid);

			if (user != null) {
				Set<ValidationProject> projects = validationProjectRepository.findByUser(user);
				log.info("Fetching list of validation projects for user " + user.toString());
				return new ResponseEntity<Set<ValidationProject>>(projects, HttpStatus.OK);
			} else {
				return new ResponseEntity<Set<ValidationProject>>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<Set<ValidationProject>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method returns the validation project with the specified id that is
	 * stored in the database
	 */
	@ApiOperation(value = "Get the validation project with the specified id from the database", nickname = "getValidationProjectById", response = ValidationProject.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/projects/validation/{pid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ValidationProject> getValidationProjectById(@PathVariable Long pid) {

		ValidationProject validationProject = null;

		try {
			validationProject = validationProjectRepository.findById(pid);

			if (validationProject != null) {
				log.info("Fetching validation project " + validationProject.toString());
				return new ResponseEntity<ValidationProject>(validationProject, HttpStatus.OK);
			} else {
				log.info("Validation project with id:" + pid + " was not found");
				return new ResponseEntity<ValidationProject>(validationProject, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method returns the validation project with the specified id that is
	 * stored in the database
	 */
	@ApiOperation(value = "Get a list of all assets for the validation project with the specified id from the database", nickname = "getAssetsByValidationProjectId")
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/projects/validation/{pid}/assets", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Set<ValidationAsset>> getAssetsByValidationProjectId(@PathVariable Long pid) {

		ValidationProject validationProject = null;

		try {
			validationProject = validationProjectRepository.findById(pid);

			if (validationProject != null) {
				Set<ValidationAsset> assets = validationProject.getAssets();
				return new ResponseEntity<Set<ValidationAsset>>(assets, HttpStatus.OK);
			} else {
				return new ResponseEntity<Set<ValidationAsset>>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<Set<ValidationAsset>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method creates a new validation project and stores it in the
	 * database
	 */
	@ApiOperation(value = "Create a new validation project and store it in the database", nickname = "createValidationProject", response = ValidationProject.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/projects/validation/{pid}/start", method = RequestMethod.POST)
	public ResponseEntity<ValidationProject> startValidationProject(@PathVariable Long pid) {

		try {
			ValidationProject project = validationProjectRepository.findOne(pid);
			
			ClassPathResource resource = new ClassPathResource("test-inputs/AgreementOffer_Demo-christos.xml");
			InputStream file = resource.getInputStream();
			
			Translate tr = new Translate();

			String[] results = tr.translateSLAtoPrismAndLisp(file);
			//log.info(results[0]); // The Prism model
			//log.info(results[1]); // The Lisp code
			
			byte[] model = results[0].getBytes();
			
			project.setModel(model);
			validationProjectRepository.save(project);
			
			return new ResponseEntity<ValidationProject>(HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method creates a new validation project and stores it in the
	 * database
	 */
	@ApiOperation(value = "Create a new validation project and store it in the database", nickname = "uploadServiceModel", response = ValidationProject.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@Transactional
	@RequestMapping(value = "/rest/api/projects/validation/{pid}/servicemodel", method = RequestMethod.POST, headers = "content-type=multipart/*")
	public ResponseEntity<ValidationProject> uploadServiceModel(@PathVariable Long pid,
			@RequestParam("files") MultipartFile[] files) {

		try {
			
			XQDataSource ds = new com.saxonica.xqj.SaxonXQDataSource();
			XQConnection con = ds.getConnection();
			
			ClassPathResource resource = new ClassPathResource("Assets.xq");
			InputStream query = resource.getInputStream();
			
			XQStaticContext ctx = con.getStaticContext();
			ctx.setBaseURI(query.toString());
			XQPreparedExpression expr = con.prepareExpression(query, ctx);
			
			File tempdir = Files.createTempDir();
			
			ValidationProject project = validationProjectRepository.findOne(pid);
			
			for(MultipartFile file : files){
				InputStream input = file.getInputStream();
				File owls = new File(tempdir.getAbsolutePath() + "/" + file.getOriginalFilename());
			    OutputStream out = new FileOutputStream(owls);
				IOUtils.copy(input,out);
				input.close();
				out.close();
			}
			
			expr.bindObject(new QName("theinput"), new String(files[0].getOriginalFilename()), null);
			expr.bindObject(new QName("thedirectory"), tempdir.getAbsolutePath(), null);
			query.close();

			String json = expr.executeQuery().getSequenceAsString(null).replace("\" ", "\"").replace(" \"", "\"").replace(", ]", " ]");
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(json);
			
			JSONObject jsonObject = (JSONObject) obj;
            JSONObject compositeServiceObj = (JSONObject) jsonObject.get("compositeService");
            
            CompositeService compositeService = new CompositeService();
            compositeService.setName((String)compositeServiceObj.get("name"));
            compositeService.setProject(project);
            /*
            compositeService.setOwls(
            	IOUtils.toByteArray(
            		new FileInputStream(
            			new File(tempdir.getAbsolutePath() + "/" + (String)compositeServiceObj.get("name") + ".owls"))));
            */
            compositeServiceRepository.save(compositeService);
			
            JSONArray atomicServices = (JSONArray) compositeServiceObj.get("atomicServices");
            Iterator<JSONObject> atomicServicesiterator = atomicServices.iterator();
            
            while (atomicServicesiterator.hasNext()) {
            	
            	JSONObject atomicServiceObj = atomicServicesiterator.next();
            	
            	AtomicService atomicService = new AtomicService();
            	atomicService.setName((String)atomicServiceObj.get("serviceName"));
            	atomicService.setCompositeService(compositeService);
            	/*
            	 * Store the newly created atomic service in the atomicService variable to use it later on
            	 */
            	AtomicService tempAtomicservice = atomicServicesRepository.save(atomicService); 
            	
            	JSONArray operations = (JSONArray) atomicServiceObj.get("operations");
            	Iterator<JSONObject> operationsIterator = operations.iterator();
            	
            	 while (operationsIterator.hasNext()) {
                 	JSONObject operationObj = operationsIterator.next();
                 	
                 	/*
                 	 * Create and store the newly created operation @ table operations
                 	 */
                 	Operation operation = new Operation();
                 	operation.setName((String)operationObj.get("operationName"));
                 	operation.setInputMessageName((String)operationObj.get("inputMessageName"));
                 	operation.setOutputMessageName((String)operationObj.get("outputMessageName"));
                 	operation.setAtomicService(tempAtomicservice);
                 	Operation tempOperation = operationsRepository.save(operation);
                 	
                 	/*
                	 * Store the operation as an asset of type operation
                	 */
                	ValidationAsset operationAsset = new ValidationAsset();
                	operationAsset.setProject(project);
                	operationAsset.setName("service." + compositeService.getName() + ".operation." + operation.getName());
                	operationAsset.setType(AssetType.OPERATION);
                	validationAssetsRepository.save(operationAsset);
                 	
                 	/*
                 	 * Create and store the output of the operation @ table outputs
                 	 */
                 	
                 	Output output = new Output();
                 	output.setOperation(tempOperation);
                 	output.setName((String)operationObj.get("outputName"));
                 	output.setType((String)operationObj.get("outputType"));
                 	outputsRepository.save(output);
                 	
                 	/*
                	 * Store the operation as an asset of type output
                	 */
                	ValidationAsset outputAsset = new ValidationAsset();
                	outputAsset.setProject(project);
                	outputAsset.setName("service." + compositeService.getName() + ".operation." + operation.getName() + ".output." + output.getName());
                	outputAsset.setType(AssetType.OUTPUT);
                	validationAssetsRepository.save(outputAsset);
                 	
                 	JSONArray inputsObj = (JSONArray) operationObj.get("inputParameters");
                	Iterator<JSONObject> inputsIterator = inputsObj.iterator();
                	
                	while(inputsIterator.hasNext()){
                		JSONObject input = inputsIterator.next();
                		
                		Input in = new Input();
                		in.setName((String)input.get("inputName"));
                		in.setOperation(tempOperation);
                		in.setType((String)input.get("inputType"));
                		inputsRepository.save(in);
                		
                		/*
                    	 * Store the operation as an asset of type input
                    	 */
                    	ValidationAsset inputAsset = new ValidationAsset();
                    	inputAsset.setProject(project);
                    	inputAsset.setName("service." + compositeService.getName() + ".operation." + operation.getName() + ".input." + in.getName());
                    	inputAsset.setType(AssetType.INPUT);
                    	validationAssetsRepository.save(inputAsset);
                		
                	}
            	 }
            }
            
			return new ResponseEntity<ValidationProject>(HttpStatus.OK);

		}catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method creates a new validation project and stores it in the
	 * database
	 */
	@ApiOperation(value = "Create a new validation project and store it in the database", nickname = "uploadServiceModel", response = ValidationProject.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@Transactional
	@RequestMapping(value = "/rest/api/projects/validation", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<ValidationProject> createValidationProject(@RequestBody ValidationProject project) {

		try {
			//project.setCreated(new Timestamp(System.currentTimeMillis()));
			project.setStatus(ValidationProjectStatus.CREATING);
			validationProjectRepository.save(project);
			return new ResponseEntity<ValidationProject>(project, HttpStatus.CREATED);

		}
		catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method creates a new validation project and stores it in the
	 * database
	 */
	@ApiOperation(value = "Create a new validation project and store it in the database", nickname = "uploadServiceModel", response = ValidationProject.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@Transactional
	@RequestMapping(value = "/rest/api/projects/validation/assets/{aid}/guardedactions", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<GuardedAction> createGuardedAction(@PathVariable Long aid, @RequestBody GuardedAction action) {

		try {
			ValidationAsset asset = validationAssetsRepository.findOne(aid);
			action.setAsset(asset);
			GuardedAction ac = guardedActionsRepository.save(action);
			return new ResponseEntity<GuardedAction>(ac, HttpStatus.CREATED);
		}
		catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method updates a validation project and stores it in the database
	 */
	@ApiOperation(value = "Update a validation project and store it in the database", nickname = "putValidationProject", response = ValidationProject.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/projects/validation", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public ResponseEntity<ValidationProject> putValidationProject(@RequestBody ValidationProject project) {

		try {

			ValidationProject p = validationProjectRepository.findById(project.getId());

			if (p != null) {
				p = validationProjectRepository.save(project);
				log.info("Validation project updated successully " + p);
				return new ResponseEntity<ValidationProject>(p, HttpStatus.CREATED);
			} else {
				log.info("Validation project with id: " + project.getId()
						+ " was not found and therefore can not be updated");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method updates the asset of a validation project and stores it in the database
	 */
	@ApiOperation(value = "Update the asset of validation project and store it in the database", nickname = "putValidationProjectAsset", response = ValidationAsset.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/projects/validation/assets", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> putValidationProjectAsset(@RequestBody ValidationAsset asset) {

		try {
			
			ValidationAsset vasset = validationAssetsRepository.findOne(asset.getId());
			
			if (vasset != null) {
				
				vasset.setRate(asset.getRate());
				vasset.setTimeunit(asset.getTimeunit());
				vasset.setSecurityproperty(asset.getSecurityproperty());
				
				validationAssetsRepository.save(vasset);
				log.info("Validation project updated successully " + vasset);
				
				return new ResponseEntity<>(HttpStatus.CREATED);
			} else {
				log.info("Validation project asset with id: " + asset.getId()
						+ " was not found and therefore can not be updated");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method creates a new validation project and stores it in the
	 * database
	 */
	@ApiOperation(value = "Create a new model and associate it with a validation project and store it in the database", nickname = "createModelForValidationProject", response = ValidationProject.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/projects/validation/{pid}/model", method = RequestMethod.POST, consumes = "multipart/form-data", headers = {
			"Accept: application/pdf" })
	public ResponseEntity<ValidationProject> postModelForValidationProject(@PathVariable Long pid,
			@RequestParam("model") MultipartFile model) {

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
	 * This method returns the validation project with the specified id that is
	 * stored in the database
	 */
	@ApiOperation(value = "Delete the validation project with the specified id from the database", nickname = "deleteValidationProjectById", response = ValidationProject.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/projects/validation/{pid}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<?> deleteValidationProjectById(@PathVariable Long pid) {

		try {
			ValidationProject p = validationProjectRepository.findById(pid);

			if (p != null) {
				validationProjectRepository.delete(p);
				log.info("Deleted validation project with id:" + pid);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				log.info("Validation project with id:" + pid + " was not found and therefore can not be deleted");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
