package uk.ac.city.toreador.rest.api.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


@ApiModel
@Entity
@Table(name = "validation_projects", catalog = "toreador")
public class ValidationProject implements Serializable{
	
	private static final long serialVersionUID = -5757552134175485875L;
	
	@ApiModelProperty(required = false, example = "36")
	@Id
	@GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@ApiModelProperty(example = "projectName", required = true)
	@Column(name="name", nullable=false)
	private String name;
	
	@ApiModelProperty(hidden = true, example = "", required = false)
	@Column(name="model", nullable=true)
	private byte[] model;
	
	@ApiModelProperty(hidden = true, example = "This is some test content for the properties file", required = false)
	@Column(name="properties", nullable=true)
	private byte[] properties;
	
	@ApiModelProperty(example = "/path/to/output/file.txt", required = true)
	@Column(name="output", nullable=false)
	private String output;
	
	@ApiModelProperty(required = false, example = "1492073991")
	@Column(name="created", nullable=true)
	private Timestamp created;
	
	@ApiModelProperty(example = "CREATED", allowableValues = "CREATED,PROCESSING,COMPLETED,ERROR")
	@Column(name="status", nullable=false)
	@Enumerated(EnumType.STRING)
	private ValidationProjectStatus status;
	
	@ApiModelProperty(required=true, hidden = false)
	@ManyToOne(optional = false, fetch=FetchType.EAGER)
	@JoinColumn(name = "user")
	private User user;
	
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

	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public ValidationProjectStatus getStatus() {
		return status;
	}
	public void setStatus(ValidationProjectStatus status) {
		this.status = status;
	}
	@JsonIgnore
	public User getUser() {
		return user;
	}
	@JsonProperty
	public void setUser(User user) {
		this.user = user;
	}
	
	public byte[] getModel() {
		return model;
	}
	public void setModel(byte[] model) {
		this.model = model;
	}
	public byte[] getProperties() {
		return properties;
	}
	public void setProperties(byte[]  properties) {
		this.properties = properties;
	}
	@Override
	public String toString() {
		return String
				.format("Validation project [id=%s, output=%s, created=%s, status=%s, user=%s]",
						id, output, created, status, user.getId());
	}
}