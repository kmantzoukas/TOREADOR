package uk.ac.city.toreador.rest.api.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@Entity
@Table(name = "operations", catalog = "toreador")
public class Operation {
	
	@ApiModelProperty(required = false, example = "36")
	@Id
	@GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@ApiModelProperty(example = "name", required = true)
	@Column(name="name", nullable=false)
	private String name;

	@ApiModelProperty(required=true, hidden = false)
	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name = "aid")
	private AtomicService atomicService;
	
	@ApiModelProperty(example = "", required = true)
	@Column(name="inputmessage", nullable=true)
	private String inputMessageName;
	
	@ApiModelProperty(example = "name", required = true)
	@Column(name="outputmessage", nullable=true)
	private String outputMessageName;
	
	@ApiModelProperty(required=false, hidden = false)
	@OneToOne(mappedBy="operation", optional = true, fetch=FetchType.EAGER)
	private Output output;
	
	@ApiModelProperty(required=false, hidden = false)
	@OneToMany(mappedBy="operation", fetch=FetchType.EAGER)
	private Set<Input> inputs;
	
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
	@JsonIgnore
	public AtomicService getAtomicService() {
		return atomicService;
	}

	public void setAtomicService(AtomicService atomicService) {
		this.atomicService = atomicService;
	}

	public String getInputMessageName() {
		return inputMessageName;
	}

	public void setInputMessageName(String inputMessageName) {
		this.inputMessageName = inputMessageName;
	}

	public String getOutputMessageName() {
		return outputMessageName;
	}

	public void setOutputMessageName(String outputMessageName) {
		this.outputMessageName = outputMessageName;
	}

	public Output getOutput() {
		return output;
	}

	public void setOutput(Output output) {
		this.output = output;
	}

	public Set<Input> getInputs() {
		return inputs;
	}

	public void setInputs(Set<Input> inputs) {
		this.inputs = inputs;
	}
	
}
