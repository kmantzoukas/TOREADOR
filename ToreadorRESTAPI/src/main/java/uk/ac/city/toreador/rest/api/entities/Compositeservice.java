package uk.ac.city.toreador.rest.api.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@Entity
@Table(name = "compositeservices", catalog = "toreador")
public class Compositeservice implements Serializable{
	
	private static final long serialVersionUID = -1610238853238741904L;

	@ApiModelProperty(required = false, example = "36")
	@Id
	@GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@ApiModelProperty(required=true, hidden = false)
	@OneToOne(optional = false, fetch=FetchType.EAGER)
	@JoinColumn(name = "pid")
	private ValidationProject project;
	
	@ApiModelProperty(example = "name", required = true)
	@Column(name="name", nullable=false)
	private String name;
	
	@ApiModelProperty(hidden = true, example = "", required = false)
	@Column(name="owls", nullable=true)
	private byte[] owls;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ValidationProject getProject() {
		return project;
	}

	public void setProject(ValidationProject project) {
		this.project = project;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getOwls() {
		return owls;
	}

	public void setOwls(byte[] owls) {
		this.owls = owls;
	}
	
}
