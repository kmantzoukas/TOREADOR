package uk.ac.city.toreador.rest.api.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQStaticContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import uk.ac.city.toreador.rest.api.entities.Asset;
import uk.ac.city.toreador.rest.api.entities.AssetSecurityPropertyPair;
import uk.ac.city.toreador.rest.api.entities.AssetsSecuritypropertiesId;
import uk.ac.city.toreador.rest.api.entities.AtomicService;
import uk.ac.city.toreador.rest.api.entities.CompositeService;
import uk.ac.city.toreador.rest.api.entities.GuardedAction;
import uk.ac.city.toreador.rest.api.entities.Input;
import uk.ac.city.toreador.rest.api.entities.Operation;
import uk.ac.city.toreador.rest.api.entities.Output;
import uk.ac.city.toreador.rest.api.entities.Project;
import uk.ac.city.toreador.rest.api.entities.User;
import uk.ac.city.toreador.rest.api.jpa.repositories.AssetSecurityPropertyPairsRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.AssetsRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.AtomicServicesRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.CompositeServicesRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.GuardedActionsRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.InputsRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.OperationsRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.OutputsRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.ProjectsRepository;
import uk.ac.city.toreador.rest.api.jpa.repositories.UsersRepository;
import uk.ac.city.toreador.validation.Translate;

@Api(tags = "Validation project resource")
@RestController
public class ProjectRESTController {

	final static Logger log = Logger.getLogger(ProjectRESTController.class);

	@Autowired
	ProjectsRepository projectsRepository;

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	CompositeServicesRepository compositeServicesRepository;

	@Autowired
	AtomicServicesRepository atomicServicesRepository;

	@Autowired
	AssetsRepository assetsRepository;

	@Autowired
	OperationsRepository operationsRepository;

	@Autowired
	OutputsRepository outputsRepository;

	@Autowired
	InputsRepository inputsRepository;

	@Autowired
	GuardedActionsRepository guardedActionsRepository;

	@Autowired
	AssetSecurityPropertyPairsRepository assetSecurityPropertyPairsRepository;

	/*
	 * This method returns the validation project with the specified id that is
	 * stored in the database
	 */
	@ApiOperation(value = "Get the validation project with the specified id from the database", nickname = "getProjectsByUser", response = Project.class)
	@ApiResponses({ @ApiResponse(code = 404, message = "The user with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The user with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/users/{uid}/projects", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Project>> getProjectsByUser(@PathVariable Integer uid) {

		try {
			User user = usersRepository.findById(uid);

			if (user != null) {
				List<Project> projects = projectsRepository.findByUserOrderByCreatedDesc(user);
				log.info("Fetching list of validation projects for user " + user.toString());
				return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Project>>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<List<Project>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method returns the validation project with the specified id that is
	 * stored in the database
	 */
	@ApiOperation(value = "Get the validation project with the specified id from the database", nickname = "getProjectById", response = Project.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Project> getProjectById(@PathVariable Integer uid, @PathVariable Integer pid) {

		Project project = null;

		try {
			project = projectsRepository.findById(pid);

			if (project != null) {
				log.info("Fetching validation project " + project.toString());
				return new ResponseEntity<Project>(project, HttpStatus.OK);
			} else {
				log.info("Validation project with id:" + pid + " was not found");
				return new ResponseEntity<Project>(project, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method creates a new validation project and stores it in the database
	 */
	@ApiOperation(value = "Create a new validation project and store it in the database", nickname = "createNewProject", response = Project.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@Transactional
	@RequestMapping(value = "/rest/api/users/{uid}/projects", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Project> createNewProject(@PathVariable Integer uid, @RequestBody Project project) {

		try {
			User user = usersRepository.findOne(uid);
			project.setCreated(new Timestamp(System.currentTimeMillis()));
			project.setStatus("CREATING");
			project.setUser(user);
			projectsRepository.save(project);
			return new ResponseEntity<Project>(project, HttpStatus.CREATED);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method creates a new validation project and stores it in the database
	 */
	@ApiOperation(value = "Create the WS-Agreement and store it in the database and create the prism model and store in the database", nickname = "translateProject", response = Project.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specifiedid was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the idprovided is not in a valid format") })
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}/translate", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<Project> translateProject(@PathVariable Integer uid, @PathVariable Integer pid) {
		try {
			Project project = projectsRepository.findOne(pid);

			/*
			 * Create the WS Agreement from the assets and guarded actions
			 */
			String xml = "";

			Set<Asset> assets = project.getAssets();

			// TODO Maria you should the code below this section
			Integer i = 1;

			xml += ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<wsag:AgreementOffer xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" "
					+ "xmlns:asrt=\"http://www.cumulus.org/certificate/model\" "
					+ "xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\" "
					+ "xmlns:wsag=\"http://schemas.ggf.org/graap/2007/03/ws-agreement\" "
					+ "xmlns:wsrf-bf=\"http://docs.oasis-open.org/wsrf/bf-2\" "
					+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
					+ "xmlns:hfp=\"http://www.w3.org/2001/XMLSchema-hasFacetAndProperty\" "
					+ "xsi:schemaLocation=\"http://schemas.ggf.org/graap/2007/03/ws-agreement file:ws-agreement-demo.xsd\"> "
					+ "<wsag:Context> " + "        <wsag:ServiceProvider>AgreementInitiator</wsag:ServiceProvider> " +
					/*
					 * "        <wsag:SLA_LC_Parameters> " + "            <asrt:IntParam> " +
					 * "                <VariableName>TooManyVio</VariableName> " +
					 * "                <InitialValue> " +
					 * "                    <asrt:Int value=\"1\"/> " +
					 * "                </InitialValue> " + "            </asrt:IntParam> " +
					 * "            <asrt:DurationParam> " +
					 * "                <VariableName>expiryTime</VariableName> " +
					 * "                <InitialValue> " + "                    <asrt:Days> " +
					 * "                        <asrt:Int value=\"10\"/> " +
					 * "                    </asrt:Days> " + "                </InitialValue> " +
					 * "            </asrt:DurationParam> " + "        </wsag:SLA_LC_Parameters> " +
					 */
					"    </wsag:Context> " + "    <wsag:Terms> " + "        <wsag:All>");

			for (Asset asset : assets) {
				for (AssetSecurityPropertyPair p : asset.getAssetSecuritypropertyPairs()) {
					if (p != null) {
						String SDTID = "SDT" + (i++); // <-- fixed values
						String assetName = asset.getName();
						String assetType = asset.getType();

						if (assetType.equals("operation")) {
							String output = asset.getOperation().getOutputmessage();
							String input = asset.getOperation().getInputmessage();

							xml += ("<wsag:ServiceDescriptionTerm wsag:Name=\"" + SDTID + "\" wsag:ServiceName=\""
									+ assetName + "\"> " + "    <wsag:Type>InternalOperation</wsag:Type> "
									+ "    <any xmlns=\"http://www.w3.org/2001/XMLSchema\">  " + "        <annotation> "
									+ "            <documentation> " + "                <wsdl:Definition name=\" "
									+ assetName + "\"> " + "<message name=\"" + input + "\"> ");

							for (Input IN : asset.getOperation().getInputs()) {
								String inputParameterName = IN.getName();
								String inputParameterType = IN.getType();

								xml += ("<part name=\"" + inputParameterName + "\" type=\"" + inputParameterType
										+ "/> ");

							}

							xml += ("            </message> " + "                    <message name=\"" + output
									+ "\"> ");

							for (uk.ac.city.toreador.rest.api.entities.Output OUT : asset.getOperation().getOutputs()) {
								// for (String InputParameters : Inputs) {
								String outputParameterName = OUT.getName();
								String outputParameterType = OUT.getType();

								xml += ("<part name=\"" + outputParameterName + "\" type=\"" + outputParameterType
										+ "/> ");
							}

							xml += (" </message> " + "                    <portType name=\"" + assetName
									+ "_PortType\"> " + "                        <operation name=\"" + assetName
									+ "Result\"> " + "                            <input message=\"" + input + "\"/> "
									+ "                            <output message=\"" + output + "\"/> "
									+ "                        </operation> " + "                    </portType> "
									+ "                    <binding> ... </binding> "
									+ "                    <service name=\"" + assetName + "\"> "
									+ "                        <port binding=\"tns:" + assetName + "_Binding\" "
									+ "                            name=\"" + assetName + "_Port\"> </port> "
									+ "                    </service> " + "                </wsdl:Definition> "
									+ "            </documentation> " + "        </annotation> " + "    </any> "
									+ "</wsag:ServiceDescriptionTerm>");
						} else if (assetType.equals("input") | assetType.equals("output")) {
							String ParamType = "";

							xml += (" <wsag:ServiceDescriptionTerm wsag:Name=\"" + SDTID + "\" wsag:ServiceName=\""
									+ assetName + "\"> " + "   <wsag:Type>DataModel</wsag:Type> "
									+ "   <any xmlns=\"http://www.w3.org/2001/XMLSchema\">  " + "       <annotation> "
									+ "           <documentation> " + "               <wsdl:types> "
									+ "                   <ComplexType name=\"" + assetName + "\"> "
									+ "                       <sequence> "
									+ "                           <element name=\"" + assetName + "\" type=\"" +
									// if type is input get the type of the
									// input parameter
									(assetType.equals("input") /* ) != null */ /* XXX */
											? asset.getInput().getType()
											// else, if type is output get the type of the
											// output parameter
											: asset.getOutput().getType())
									+ "\"/> " + "                       </sequence> "
									+ "                   </ComplexType> " + "                </wsdl:types>  "
									+ "            </documentation> " + "        </annotation> " + "    </any> "
									+ "</wsag:ServiceDescriptionTerm>");
						}

						String GTname = asset.getName() + "_" + p.getSecurityProperty().getName();

						String SecurityProperty = p.getSecurityProperty().getName();
						String Asset = asset.getName();
						String rate = p.getRate();

						xml += ("<wsag:GuaranteeTerm wsag:Name=\"" + GTname + "\" wsag:Obligated=\"ServiceProvider\">"
								+ "             <wsag:ServiceLevelObjective> "
								+ "                 <wsag:CustomServiceLevel> "
								+ "                     <wsag:DeclarativeLevel> "
								+ "                         <wsag:SLO_Category>" + SecurityProperty
								+ "</wsag:SLO_Category> "
								+ "                         <wsag:ServiceAsset>SDT1/wsdl:definition/portType/" + "/"
								+ Asset + "</wsag:ServiceAsset>" + "                     </wsag:DeclarativeLevel> "
								+ "                     <wsag:ProceduralLevel> "
								+ "                         <wsag:SLOTemplate wsag:Name=\"" + SecurityProperty + "\"> "
								+ "                             <wsag:SLOTemplateParameters> " /*
																								 * <-- Not needed for
																								 * validation project.
																								 */
								/* <-- Needed for monitoring project. */
								+ "                                 <wsag:SLOTemplateParameter name=\"" + "\" "
								+ "                                     value=\"" + "\"/>  "
								+ "                                 <wsag:SLOTemplateParameter name=\"Metric\" value=\""
								+ "\"/>  " + "                             </wsag:SLOTemplateParameters> "
								+ "                         </wsag:SLOTemplate> "
								+ "                         <wsag:Assertion ID=\"" + GTname + "\">  "
								+ "                             <InterfaceDeclr> "
								+ "                                 <ID></ID> "
								+ "                                 <ProviderRef></ProviderRef> "
								+ "                                 <Interface> "
								+ "                                     <InterfaceRef> "
								+ "                                         <InterfaceLocation></InterfaceLocation> "
								+ "                                     </InterfaceRef> "
								+ "                                 </Interface> "
								+ "                             </InterfaceDeclr> "
								+ "                             <VariableDeclr> "
								+ "                                 <varName></varName> "
								+ "                                 <varType></varType> "
								+ "                             </VariableDeclr> "
								+ "                             <Guaranteed ID=\"\" type=\"\"> "
								+ "                                 <quantification> "
								+ "                                     <quantifier>forall</quantifier> "
								+ "                                     <timeVariable> "
								+ "                                         <varName></varName> " /*
																									 * XXX - what's this
																									 * nameless
																									 * variable?????
																									 */
								+ "                                         <varType></varType> "
								+ "                                     </timeVariable> "
								+ "                                 </quantification> "
								+ "                                 <postcondition> "
								+ "                                     <atomicCondition> "
								+ "                                         <eventCondition> "
								+ "                                             <event> "
								+ "                                                 <eventID> "
								+ "                                                     <varName></varName> " /*
																												 * XXX -
																												 * what'
																												 * s
																												 * this
																												 * nameless
																												 * variable
																												 * ?????
																												 */
								+ "                                                 </eventID> "
								+ "                                             </event> "
								+ "                                         </eventCondition> "
								+ "                                     </atomicCondition> "
								+ "                                 </postcondition> "
								+ "                             </Guaranteed> "
								+ "                         </wsag:Assertion> "
								+ "                     </wsag:ProceduralLevel> "
								+ "                 </wsag:CustomServiceLevel> "
								+ "             </wsag:ServiceLevelObjective> "
								+ "             <wsag:BusinessValueList> "
								+ "                 <wsag:CustomBusinessValue> ");

						for (GuardedAction gaction : p.getGuardedActions()) {
							String guard = gaction.getGuard();
							Integer PenaltyValue = gaction.getPenalty();
							xml += ("              <wsag:GuardedAction> " + ((gaction.getAction().equals("RENEGOTIATE"))
									? "<wsag:ReNegotiate/>"
									: ((gaction.getAction().equals("PENALTY"))
											? ("<wsag:Penalty> " + "<wsag:Value>" + PenaltyValue + "</wsag:Value>"
													+ "<wsag:ValueUnit>GBP</wsag:ValueUnit>" + "</wsag:Penalty>")
											: ("                     <wsag:Other> "
													+ "                         <wsag:ActionName>" + gaction.getName()
													+ "</wsag:ActionName> " + "                     </wsag:Other> ")))
									+ "                         <wsag:ValueExpr> " + guard
									+ "                         </wsag:ValueExpr> "
									+ "                     </wsag:GuardedAction> ");
						}

						xml += ("                     <wsag:Rate> <asrt:" + p.getTimeunit() + ">" + rate + "</asrt:"
								+ p.getTimeunit() + "></wsag:Rate> " + "                 </wsag:CustomBusinessValue> "
								+ "             </wsag:BusinessValueList> " + "         </wsag:GuaranteeTerm>");
					}
				}
			}

			xml += ("        </wsag:All> " + "</wsag:Terms> " + "</wsag:AgreementOffer>");

			project.setWsagreement(xml.getBytes());
			project.setStatus("CREATED");
			projectsRepository.save(project);

			/*
			 * Translate the WS Agreement, create the prism model and store it into the
			 * database
			 */
			InputStream file = IOUtils.toInputStream(xml);
			Translate tr = new Translate();

			String[] results = tr.translateSLAtoPrismAndLisp(file);
			// log.info(results[0]); // The Prism model
			// log.info(results[1]); // The Lisp code

			byte[] model = results[0].getBytes();

			project.setModel(model);
			projectsRepository.save(project);

			return new ResponseEntity<Project>(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();

			// log.error(e.pr);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method updates a validation project and stores it in the database
	 */
	@ApiOperation(value = "Update a project and store it in the database", nickname = "updateProject", response = Project.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Project> updateProject(@PathVariable Integer uid, @PathVariable Integer pid,
			@RequestBody Project project) {

		try {

			Project p = projectsRepository.findById(pid);

			p.setName(project.getName());
			p.setPropertyCategoryCatalog(project.getPropertyCategoryCatalog());
			p.setStatus(project.getStatus());

			projectsRepository.save(p);
			log.info("Validation project updated successully " + p);
			return new ResponseEntity<Project>(p, HttpStatus.CREATED);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Delete the project with the id specified", nickname = "deleteProjectById")
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@Transactional
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteProjectById(@PathVariable Integer uid, @PathVariable Integer pid) {

		try {
			projectsRepository.delete(pid);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Get a file as an array of bytes for a specific project", nickname = "getFileFromProject")
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}/{file}", method = RequestMethod.GET)
	public ResponseEntity<String> getFileFromProjectById(@PathVariable Integer uid, @PathVariable Integer pid,
			@PathVariable String file) {
	    
		try {

			Project p = projectsRepository.findById(pid);
			byte[] f = null;
			
			final HttpHeaders headers = new HttpHeaders();
			
			switch (file) {
			case "properties":
				f = p.getProperties();
				headers.setContentType(MediaType.TEXT_PLAIN);
				break;
			case "model":
				f = p.getModel();
				headers.setContentType(MediaType.TEXT_PLAIN);
				break;
			case "validationoutput":
				f = p.getValidationoutput();
				headers.setContentType(MediaType.TEXT_PLAIN);
				break;
			case "wsagreement":
				f = p.getWsagreement();
				headers.setContentType(MediaType.APPLICATION_XML);
				break;
			default:
				f = null;
				break;
			}
			if (f != null) {
				return new ResponseEntity<String>(new String(f,"UTF-8"), headers, HttpStatus.OK);
			} else {
				log.info(file + " for project with id " + pid + " does not exist in the database");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Get the OWLS file as an array of bytes for a specific compostire service", nickname = "getOwlsFromCompositeService")
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}/compositeservices/{cid}/owls", method = RequestMethod.GET)
	public ResponseEntity<String> getOwlsFromCompositeService(@PathVariable Integer uid, @PathVariable Integer pid,
			@PathVariable Integer cid) {

		try {

			CompositeService cservice = compositeServicesRepository.findOne(cid);

			if (cservice != null) {

				byte[] owls = cservice.getOwls();

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_XML);

				log.info("OWL-S for composite service with id " + cservice.getId() + " has been retreived successully");
				return new ResponseEntity<String>(new String(owls,"UTF-8"), headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Get the OWLS file as an array of bytes for a specific compostire service", nickname = "getOwlsFromCompositeService")
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}/compositeservices/{cid}/atomicservices/{aid}", method = RequestMethod.GET)
	public ResponseEntity<String> getOwlFromAtomicService(@PathVariable Integer uid, @PathVariable Integer pid,
			@PathVariable Integer cid, @PathVariable Integer aid) {

		try {

			AtomicService aservice = atomicServicesRepository.findOne(aid);

			if (aservice != null) {

				byte[] owl = aservice.getOwl();

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_XML);
				log.info("OWL for atomic service with id " + aservice.getId() + " has been retreived successully");
				return new ResponseEntity<String>(new String(owl,"UTF-8"), headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Delete the pair of (asset,security property) with the specified ids (asset id, security property id)", nickname = "deleteAssetSecurityPropertyPairById")
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@Transactional
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}/assetsecuritypropertypair/{aid},{spid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAssetSecurityPropertyPairById(@PathVariable Integer uid, @PathVariable Integer pid,
			@PathVariable Integer aid, @PathVariable Integer spid) {

		try {
			assetSecurityPropertyPairsRepository.delete(new AssetsSecuritypropertiesId(aid, spid));
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Delete the pair of (asset,security property) with the specified ids (asset id, security property id)", nickname = "deleteAssetSecurityPropertyPairById")
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@Transactional
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}/assetsecuritypropertypair/{aid},{spid}/guardedactions/{gid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteGuardedActionById(@PathVariable Integer uid, @PathVariable Integer pid,
			@PathVariable Integer aid, @PathVariable Integer spid, @PathVariable Integer gid) {

		try {
			guardedActionsRepository.delete(gid);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method creates a new validation project and stores it in the database
	 */
	@ApiOperation(value = "Upload a properties file for a validation project and store it in the database", nickname = "uploadServiceModelItem")
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@Transactional
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}/servicemodelitem", method = RequestMethod.POST, headers = "content-type=multipart/*")
	public ResponseEntity<String> uploadServiceModelItem(@PathVariable Integer uid, @PathVariable Integer pid,
			@RequestParam("file") MultipartFile file) {

		try {

			Project project = projectsRepository.findOne(pid);
			if (project != null) {

				File tempdir = Files.createTempDir();
				InputStream input = file.getInputStream();
				File tmpfile = new File(tempdir.getAbsolutePath() + "/" + file.getOriginalFilename());
				OutputStream out = new FileOutputStream(tmpfile);
				IOUtils.copy(input, out);
				input.close();
				out.close();

				return new ResponseEntity<String>(tmpfile.getAbsolutePath(), HttpStatus.CREATED);
			} else {
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
	@ApiOperation(value = "Create a new validation project and store it in the database", nickname = "uploadServiceModel")
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@Transactional
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}/servicemodel", method = RequestMethod.POST)
	public ResponseEntity<Project> uploadServiceModel(@PathVariable Integer uid, @PathVariable Integer pid,
			@RequestBody String[] paths) {

		try {

			XQDataSource ds = new com.saxonica.xqj.SaxonXQDataSource();
			XQConnection con = ds.getConnection();

			ClassPathResource resource = new ClassPathResource("Assets.xq");
			InputStream query = resource.getInputStream();

			XQStaticContext ctx = con.getStaticContext();
			ctx.setBaseURI(query.toString());
			XQPreparedExpression expr = con.prepareExpression(query, ctx);

			File tempdir = Files.createTempDir();

			Project project = projectsRepository.findOne(pid);

			File[] files = new File[paths.length];
			int counter = 0;

			for (String path : paths) {
				log.info("Reading file with filename:" + path);
				File f = new File(path.trim().replaceAll("\"", ""));
				InputStream in = new FileInputStream(f);
				OutputStream out = new FileOutputStream(tempdir.getAbsolutePath() + File.separatorChar + f.getName());
				IOUtils.copy(in, out);
				files[counter] = f;
				counter++;
			}

			expr.bindObject(new QName("theinput"), new String(files[0].getName()), null);
			expr.bindObject(new QName("thedirectory"), tempdir.getAbsolutePath(), null);
			query.close();

			String json = expr.executeQuery().getSequenceAsString(null).replace("\" ", "\"").replace(" \"", "\"")
					.replace(", ]", " ]");

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(json);

			JSONObject jsonObject = (JSONObject) obj;
			JSONObject compositeServiceObj = (JSONObject) jsonObject.get("compositeService");

			CompositeService compositeService = new CompositeService();
			compositeService.setName((String) compositeServiceObj.get("name"));
			compositeService.setProject(project);

			compositeService.setOwls(IOUtils.toByteArray(new FileInputStream(
					new File(tempdir.getAbsolutePath() + File.separatorChar + files[0].getName()))));

			compositeServicesRepository.save(compositeService);

			JSONArray atomicServices = (JSONArray) compositeServiceObj.get("atomicServices");
			Iterator<?> atomicServicesiterator = atomicServices.iterator();

			while (atomicServicesiterator.hasNext()) {

				JSONObject atomicServiceObj = (JSONObject) atomicServicesiterator.next();

				AtomicService atomicService = new AtomicService();
				atomicService.setName((String) atomicServiceObj.get("serviceName"));
				atomicService.setCompositeService(compositeService);
				atomicService.setOwl(
						IOUtils.toByteArray(new FileInputStream(new File(tempdir.getAbsolutePath() + File.separatorChar
								+ ((String) atomicServiceObj.get("serviceName")).replace("Service", "") + ".owl"))));
				/*
				 * Store the newly created atomic service in the atomicService variable to use
				 * it later on
				 */
				AtomicService tempAtomicservice = atomicServicesRepository.save(atomicService);

				JSONArray operations = (JSONArray) atomicServiceObj.get("operations");
				Iterator<?> operationsIterator = operations.iterator();

				while (operationsIterator.hasNext()) {
					JSONObject operationObj = (JSONObject) operationsIterator.next();

					/*
					 * Store the operation as an asset of type operation
					 */
					Asset operationAsset = new Asset();
					operationAsset.setProject(project);
					operationAsset.setName("service_" + compositeService.getName() + "_operation_"
							+ operationObj.get("operationName"));
					operationAsset.setType("OPERATION");
					Asset tempOperationAsset = assetsRepository.save(operationAsset);

					/*
					 * Create and store the newly created operation @ table operations
					 */
					Operation operation = new Operation();
					operation.setName((String) operationObj.get("operationName"));
					operation.setInputmessage((String) operationObj.get("inputMessageName"));
					operation.setOutputmessage((String) operationObj.get("outputMessageName"));
					operation.setAtomicService(tempAtomicservice);
					operation.setAsset(tempOperationAsset);
					Operation tempOperation = operationsRepository.save(operation);

					/*
					 * Store an asset of type output
					 */
					Asset outputAsset = new Asset();
					outputAsset.setProject(project);
					outputAsset.setName("service_" + compositeService.getName() + "_operation_" + operation.getName()
							+ "_output_" + (String) operationObj.get("outputName"));
					outputAsset.setType("OUTPUT");
					Asset tempOutputAsset = assetsRepository.save(outputAsset);

					/*
					 * Create and store the output of the operation @ table outputs
					 */

					Output output = new Output();
					output.setOperation(tempOperation);
					output.setName((String) operationObj.get("outputName"));
					output.setType((String) operationObj.get("outputType"));
					output.setAsset(tempOutputAsset);
					outputsRepository.save(output);

					JSONArray inputsObj = (JSONArray) operationObj.get("inputParameters");
					Iterator<?> inputsIterator = inputsObj.iterator();

					while (inputsIterator.hasNext()) {
						JSONObject inputObj = (JSONObject) inputsIterator.next();

						/*
						 * Store the operation as an asset of type input
						 */
						Asset inputAsset = new Asset();
						inputAsset.setProject(project);
						inputAsset.setName("service_" + compositeService.getName() + "_operation_" + operation.getName()
								+ "_input_" + (String) inputObj.get("inputName"));
						inputAsset.setType("INPUT");
						Asset tempInputAsset = assetsRepository.save(inputAsset);

						Input input = new Input();
						input.setName((String) inputObj.get("inputName"));
						input.setOperation(tempOperation);
						input.setType((String) inputObj.get("inputType"));
						input.setAsset(tempInputAsset);
						inputsRepository.save(input);
					}
				}
			}

			return new ResponseEntity<Project>(HttpStatus.OK);

		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getCause());
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method creates a new validation project and stores it in the database
	 */
	@Transactional
	@RequestMapping(value = "/rest/api/projects/validation/{pid}/uploadtest", method = RequestMethod.POST, headers = "content-type=multipart/*")
	public ResponseEntity<?> uploadTestFile(@PathVariable Long pid, @RequestParam("files") MultipartFile[] files) {

		for (MultipartFile file : files) {
			log.info(file.getOriginalFilename() + " - " + file.getName() + " - " + file.getSize());
		}

		return new ResponseEntity<Project>(HttpStatus.CREATED);
	}

	/*
	 * This method updates the asset of a validation project and stores it in the
	 * database
	 */
	@ApiOperation(value = "Update the asset of validation project and store it in the database", nickname = "createAssetSecurityPropertyPair", response = Asset.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}/assetsecuritypropertypair", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> createAssetSecurityPropertyPair(
			@RequestBody AssetSecurityPropertyPair assetSecurityPropertyPair) {

		try {
			assetSecurityPropertyPairsRepository.save(assetSecurityPropertyPair);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Update the asset of validation project and store it in the database", nickname = "createAssetSecurityPropertyPair", response = Asset.class)
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}/guardedactions", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> createGuardedAction(@RequestBody GuardedAction guardedAction) {

		try {
			guardedActionsRepository.save(guardedAction);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * This method creates a new validation project and stores it in the database
	 */
	@ApiOperation(value = "Upload a properties file for a validation project and store it in the database", nickname = "uploadPropertiesFile")
	@ApiResponses({
			@ApiResponse(code = 404, message = "The validation project with the specified id was not found in the databse"),
			@ApiResponse(code = 400, message = "The validation project with the id provided is not in a valid format") })
	@Transactional
	@RequestMapping(value = "/rest/api/users/{uid}/projects/{pid}/properties", method = RequestMethod.POST, headers = "content-type=multipart/*")
	public ResponseEntity<?> uploadPropertiesFile(@PathVariable Integer uid, @PathVariable Integer pid,
			@RequestParam("file") MultipartFile file) {

		try {

			Project project = projectsRepository.findOne(pid);
			if (project != null) {
				project.setProperties(file.getBytes());
				projectsRepository.save(project);
				return new ResponseEntity<Project>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<Project>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
