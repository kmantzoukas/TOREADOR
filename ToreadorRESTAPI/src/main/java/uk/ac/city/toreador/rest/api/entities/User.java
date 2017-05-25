package uk.ac.city.toreador.rest.api.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@ApiModel(value = "User", description = "An entity that represents a user.")
@Entity
@Table(name = "users", catalog = "toreador")
public class User implements Serializable{
	
	private static final long serialVersionUID = 7868533363937836866L;
	
	@ApiModelProperty(example = "2", readOnly = true, required = false)
	@Id
	@GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@ApiModelProperty(example = "John", readOnly = false, required = true)
	@Column(name="name", nullable=false)
	private String name;
	
	@ApiModelProperty(example = "Doe", readOnly = false, required = true)
	@Column(name="surname", nullable=false)
	private String surname;
	
	@ApiModelProperty(example = "jdoe", readOnly = false, required = true)
	@Column(name="username", nullable=false)
	private String username;

	@ApiModelProperty(example = "secr3t", readOnly = false, required = true)
	@JsonIgnore
	@Column(name="password", nullable=false)
	private String password;
	
	@ApiModelProperty(hidden = true)
	@OneToMany(mappedBy = "user", fetch=FetchType.EAGER)
	private List<ValidationProject> validationProjects = new ArrayList<ValidationProject>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
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
	
	public List<ValidationProject> getValidationProjects() {
		return validationProjects;
	}
	public void setValidationProjects(List<ValidationProject> validationProjects) {
		this.validationProjects = validationProjects;
	}
	@Override
	public String toString() {
		return String
				.format("User [id=%s, name=%s, surname=%s, username=%s, validation_projects=%s]",
						id, name, surname, username, validationProjects);
	}
}
