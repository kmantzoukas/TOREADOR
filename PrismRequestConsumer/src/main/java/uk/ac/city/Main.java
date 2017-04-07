package uk.ac.city;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:db.properties")
@ConfigurationProperties(prefix = "db")
public class Main {
	
	private String url;
	private String username;
	private String password;
	private String driver;
	
	public static void main(String[] args) throws InterruptedException {
		
		ApplicationContext ctx = SpringApplication.run(Main.class, args);
		final PrismRequestRepository repostiory = ctx.getBean(PrismRequestRepository.class);
		
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
		  @Override
		  public void run() {
		    System.out.println(repostiory.findById(1L));
		  }
		}, 0, 2, TimeUnit.SECONDS);
	}

	@Bean
	@Primary
	public DataSource dataSource() {
		return DataSourceBuilder
				.create()
				.username(username)
				.password(password)
				.url(url)
				.driverClassName(driver)
				.build();
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

}
