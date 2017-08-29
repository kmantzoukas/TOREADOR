package uk.ac.city.toreador;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.google.common.io.Files;

import uk.ac.city.toreador.rest.api.entities.Project;
import uk.ac.city.toreador.rest.api.jpa.repositories.ProjectsRepository;

@ComponentScan(basePackages = { "uk.ac.city.toreador" })
@EnableJpaRepositories(basePackages = { "uk.ac.city.toreador.rest.api.jpa.repositories" })
@EntityScan("uk.ac.city.toreador.rest.api.entities")
@SpringBootApplication
public class PrismRequestConsumer {

	final static Logger log = Logger.getLogger(PrismRequestConsumer.class);
	
	@Value("${db.url}")
	private String url;
	@Value("${db.username}")
	private String username;
	@Value("${db.password}")
	private String password;
	@Value("${db.driver}")
	private String driver;
	
	private static String binary = "prism";

	public static void main(String[] args) throws InterruptedException, IOException {

		ApplicationContext ctx = SpringApplication.run(PrismRequestConsumer.class, args);
		final ProjectsRepository  repository = ctx
				.getBean(ProjectsRepository.class);

		ScheduledExecutorService exec = Executors
				.newSingleThreadScheduledExecutor();

		exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run(){
				
				/*
				 * Fetch from the database all the prism requests that have been
				 * created and haven't been yet processed
				 */
				Set<Project> projects = repository.findByStatus("STARTED");

				for (final Project project : projects) {
					
					log.info("New validation project received...");
					
					/*
					 * Set the status of the request from CREATED to PROCESSING
					 * and store it to the database
					 */
					project.setStatus("PROCESSING");
					repository.save(project);
					
					final File tempdir = Files.createTempDir();
					
					try {
						File model = new File(tempdir.getAbsolutePath() + File.separator +"model.prism");
						log.info(String.format("Writting prism model file in %s ..", model.getAbsolutePath()));
						
						File properties = new File(tempdir.getAbsolutePath() + File.separator +"properties.props");
						log.info(String.format("Writting properties file in %s ...", properties.getAbsolutePath()));
						
						FileUtils.writeByteArrayToFile(model, project.getModel());
						FileUtils.writeByteArrayToFile(properties, project.getProperties());
					} catch (IOException e) {
						log.error(e.getMessage());
					}
					
					/*
					 * Open and read the properties file
					 */
					InputStream input = new ByteArrayInputStream(project.getProperties());
					BufferedReader br = new BufferedReader(new InputStreamReader(input));

					String strLine;
					int counter = 0;

					try {
						while ((strLine = br.readLine()) != null)   {
							if(strLine.length() != 0)
								counter++;
						}
					} catch (IOException e) {
						log.error(e.getMessage());
					}finally {
						try {
							br.close();
						} catch (IOException e) {
							log.error("Unable to close the buffer reader for the properties file");
						}
					}
					
					final int iterations = counter;
					
					new Thread() {
						public void run() {
							try {
								for(int i=1;i<=iterations;i++) {
									final ProcessBuilder pb = new ProcessBuilder(
											binary, 
											tempdir.getAbsolutePath() + File.separator + "model.prism",
											tempdir.getAbsolutePath() + File.separator + "properties.props",
											"-prop", 
											String.valueOf(i),
											"-const",
											"MaxInteger=30",
											"-exportresults",
											tempdir.getAbsolutePath() + File.separator +"output-" + i + ".txt");
									
									Object[] cmd = (Object[]) pb.command().toArray();
									
									StringBuffer buf = new StringBuffer();
									for(Object item : cmd){
										buf.append(item.toString() + " ");
									}
									log.info(String.format("Running command %s ...",buf.toString()));
									
									Process p = pb.start();
									pb.redirectErrorStream(false);
									
									try {
										int result = p.waitFor();
										/*
										 * 0 indicates successful execution of the
										 * process
										 */
										if (result == 0) {
											log.info(String.format("Validation project with id %d has been processed successfully.", project.getId()));
										} else {
											log.error(String.format("Prism request with id %d has not been processed successfully.", project.getId())); 
											BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
											StringBuilder builder = new StringBuilder();
											String line = null;
											while ((line = reader.readLine()) != null) {
												builder.append(line);
												builder.append(System.getProperty("line.separator"));
											}
											log.error(builder.toString());
											/*
											 * Set the status of the request to ERROR and store it in the corresponding file
											 */
											project.setStatus("ERROR");
											IOUtils.copy(IOUtils.toInputStream(builder.toString(), Charsets.toCharset("UTF-8")), new FileOutputStream(tempdir.getAbsolutePath() + File.separator +"output-" + i + ".txt"));
											repository.save(project);
											/*
											 * Destroy the process and every subprocess that has started
											 */
											p.destroyForcibly();
											
										}

									} catch (InterruptedException e) {
										project.setStatus("ERROR");
										repository.save(project);
										log.error(e.getMessage());
									}
								}
								
								File  output = new File(tempdir.getAbsolutePath() + File.separator + "output.txt");
								
								for(int i=1;i<=iterations;i++) {
									FileUtils.writeStringToFile(output, "\n############### Property -" + i + "- ###############\n",Charsets.toCharset("UTF-8"), true);
									File partialOutput = new File(tempdir.getAbsolutePath() + File.separator + "output-" + i + ".txt");
									FileUtils.writeStringToFile(output, FileUtils.readFileToString(partialOutput,Charsets.toCharset("UTF-8")), Charsets.toCharset("UTF-8"), true);
								}
								
								project.setValidationoutput(IOUtils.toByteArray(new FileInputStream(output)));
								if(repository.findOne(project.getId()).getStatus().equals("PROCESSING")) {
									project.setStatus("COMPLETED");
								}
								repository.save(project);
								
								
							} catch (IOException e) {
								project.setStatus("ERROR");
								repository.save(project);
								log.error(e.getMessage());
							}
						}
					}.start();
				}
			}
		}, 0, 3, TimeUnit.SECONDS);
	}

	@Bean
	@Primary
	public DataSource dataSource() {
		return DataSourceBuilder.create().username(username).password(password)
				.url(url).driverClassName(driver).build();
	}
}
