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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@Entity
@Table(name = "atomicservices", catalog = "toreador")
public class AtomicService {
	
	@ApiModelProperty(required = false, example = "36")
	@Id
	@GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name = "cid")
	private CompositeService compositeService;
	
	@ApiModelProperty(example = "name", required = true)
	@Column(name="name", nullable=false)
	private String name;
	
	@ApiModelProperty(hidden = true, example = "", required = false)
	@Column(name="owl", nullable=true)
	private byte[] owl;
	
	@ApiModelProperty(hidden = true)
	@OneToMany(mappedBy = "atomicService", fetch=FetchType.EAGER)
	private Set<Operation> operations;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@JsonIgnore
	public CompositeService getCompositeService() {
		return compositeService;
	}

	public void setCompositeService(CompositeService compositeService) {
		this.compositeService = compositeService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getOwl() {
		return owl;
	}

	public void setOwl(byte[] owl) {
		this.owl = owl;
	}

	public Set<Operation> getOperations() {
		return operations;
	}

	public void setOperations(Set<Operation> operations) {
		this.operations = operations;
	}
	
}
