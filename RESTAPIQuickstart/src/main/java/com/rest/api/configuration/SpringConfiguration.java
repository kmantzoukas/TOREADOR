package com.rest.api.configuration;

import java.beans.PropertyVetoException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackages = {"com.rest.api.jpa.repositories"})
@ComponentScan(basePackages = "com.rest.api")
@PropertySource("classpath:db.properties")
public class SpringConfiguration {
	
	@Autowired
	Environment env;
	
	@Bean
	public DataSource dataSourceBean() throws NamingException,PropertyVetoException {

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl(env.getProperty("db.url"));
		cpds.setUser(env.getProperty("db.username"));
		cpds.setPassword(env.getProperty("db.password"));

		return cpds;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException, PropertyVetoException {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.MYSQL);
		vendorAdapter.setShowSql(Boolean.TRUE);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setPackagesToScan(new String[] { "com.rest.api.entities" });
		factory.setDataSource(dataSourceBean());
		factory.setJpaVendorAdapter(vendorAdapter);

		return factory;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() throws NamingException, PropertyVetoException {

		final JpaTransactionManager txManager = new JpaTransactionManager();

		txManager.setEntityManagerFactory(entityManagerFactory().getObject());
		txManager.setDataSource(dataSourceBean());

		return txManager;
	}
}