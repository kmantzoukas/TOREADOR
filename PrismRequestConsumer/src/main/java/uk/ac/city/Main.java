package uk.ac.city;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = { "classpath:db.properties","classpath:prism.properties" })
@ConfigurationProperties(prefix = "db")
public class Main {

	final static Logger log = Logger.getLogger(Main.class);

	private String url;
	private String username;
	private String password;
	private String driver;
	private static String basedir;
	private static String binary;

	public static void main(String[] args) throws InterruptedException {

		ApplicationContext ctx = SpringApplication.run(Main.class, args);
		final PrismRequestRepository repository = ctx
				.getBean(PrismRequestRepository.class);

		ScheduledExecutorService exec = Executors
				.newSingleThreadScheduledExecutor();

		exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				/*
				 * Fetch from the database all the prism requests that have been
				 * created and haven't been yet processed
				 */
				List<PrismRequest> requests = repository
						.findByStatus(Status.CREATED);

				for (final PrismRequest request : requests) {
					/*
					 * Set the status of the request from CREATED to PROCESSING
					 * and store it to the database
					 */
					request.setStatus(Status.PROCESSING);
					repository.save(request);
					/*
					 * Log the fact that the request's status has changed
					 */
					log.info(String.format("Prism request has been read from the database %s",request.toString()));

					final ProcessBuilder pb = new ProcessBuilder(
							binary, 
							"-javamaxmem",
							"10g", 
							basedir + "/models/sample1-pta-simple.prism",
							basedir + "/properties/paper.props",
							"-prop", 
							"1", 
							"-exportresults",
							basedir + "/prism/output/output.txt");

					new Thread() {
						public void run() {
							try {
								log.info(pb.command().toString());
								Process p = pb.start();
								pb.redirectErrorStream(false);
								try {
									int result = p.waitFor();
									/*
									 * 0 indicates successful execution of the
									 * process
									 */
									if (result == 0) {
										request.setStatus(Status.COMPLETED);
										repository.save(request);
										log.info(String.format("Prism request with id %i has been processed successfully.", request.getId()));
									} else {
										log.error(String.format("Prism request with id %i has not been processed successfully.", request.getId())); 
										BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
										StringBuilder builder = new StringBuilder();
										String line = null;
										while ((line = reader.readLine()) != null) {
											builder.append(line);
											builder.append(System.getProperty("line.separator"));
										}
										log.error(builder.toString());
										/*
										 * Set the status of the request to ERROR and store it in the database
										 */
										request.setStatus(Status.ERROR);
										repository.save(request);
										/*
										 * Destroy the process and every subprocess that has started
										 */
										p.destroyForcibly();
										
									}

								} catch (InterruptedException e) {
									request.setStatus(Status.ERROR);
									repository.save(request);
									log.error(e.getMessage());
								}

							} catch (IOException e) {
								request.setStatus(Status.ERROR);
								repository.save(request);
								log.error(e.getMessage());
							}
						}
					}.start();
				}
			}
		}, 0, 2, TimeUnit.SECONDS);
	}

	@Bean
	@Primary
	public DataSource dataSource() {
		return DataSourceBuilder.create().username(username).password(password)
				.url(url).driverClassName(driver).build();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getBasedir() {
		return basedir;
	}

	public void setBasedir(String basedir) {
		Main.basedir = basedir;
	}

	public static String getBinary() {
		return binary;
	}

	public static void setBinary(String binary) {
		Main.binary = binary;
	}

}
