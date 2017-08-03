package uk.ac.city.toreador.rest.api.configuration;

import java.beans.PropertyVetoException;
import java.sql.Timestamp;
import java.time.LocalDate;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableJpaRepositories(basePackages = { "uk.ac.city.toreador.rest.api.jpa.repositories" })
@ComponentScan(basePackages = { "uk.ac.city.toreador.rest.api" })
@PropertySource({ "classpath:db.properties", "classpath:swagger.api.properties" })
@EnableWebMvc
@EnableSwagger2
public class SpringConfiguration {

	private int maxUploadSizeInMb = 5 * 1024 * 1024;

	@Autowired
	Environment env;

	@Bean
	public Docket toreadorAPI() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.any()).build()
				.directModelSubstitute(LocalDate.class, String.class)
				.directModelSubstitute(Timestamp.class, String.class)
				.useDefaultResponseMessages(false)
				.genericModelSubstitutes(ResponseEntity.class)
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {

		final String title = env.getProperty("swagger.api.title");
		final String description = env.getProperty("swagger.api.description");
		final String name = env.getProperty("swagger.api.developer.name");
		final String email = env.getProperty("swagger.api.developer.email");
		final String license = env.getProperty("swagger.api.license");
		final String url = env.getProperty("swagger.api.license.url");
		final String version = env.getProperty("swagger.api.version");

		return new ApiInfoBuilder().title(title).description(description)
				.contact(new Contact(name, "", email)).license(license)
				.licenseUrl(url).version(version).build();
	}

	@Bean
	public DataSource dataSourceBean() throws NamingException,
			PropertyVetoException {

		ComboPooledDataSource cpds = new ComboPooledDataSource();

		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl(env.getProperty("db.url"));
		cpds.setUser(env.getProperty("db.username"));
		cpds.setPassword(env.getProperty("db.password"));

		return cpds;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
			throws NamingException, PropertyVetoException {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.MYSQL);
		vendorAdapter.setShowSql(Boolean.FALSE);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setPackagesToScan(new String[] { "uk.ac.city.toreador.rest.api.entities" });
		factory.setDataSource(dataSourceBean());
		factory.setJpaVendorAdapter(vendorAdapter);

		return factory;
	}

	@Bean
	public PlatformTransactionManager transactionManager()
			throws NamingException, PropertyVetoException {

		final JpaTransactionManager txManager = new JpaTransactionManager();

		txManager.setEntityManagerFactory(entityManagerFactory().getObject());
		txManager.setDataSource(dataSourceBean());

		return txManager;
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		
		CommonsMultipartResolver cmr = new CommonsMultipartResolver();
		cmr.setMaxUploadSize(maxUploadSizeInMb);
		
		return cmr;

	}
}