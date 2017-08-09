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

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "User", description = "An entity that represents the output of an operation.")
@Entity
@Table(name = "inputs", catalog = "toreador")
public class Input implements Serializable{
	
	private static final long serialVersionUID = -6891363427401851335L;

	@ApiModelProperty(example = "2", readOnly = true, required = false)
	@Id
	@GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@ApiModelProperty(example = "", readOnly = false, required = true)
	@Column(name="name", nullable=false)
	private String name;
	
	@ApiModelProperty(example = "OPERATION", allowableValues = "")
	@Column(name="type", nullable=false)
	private String type;
	
	@ApiModelProperty(required=true, hidden = false)
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "oid")
	private Operation operation;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@JsonIgnore
	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	
}
