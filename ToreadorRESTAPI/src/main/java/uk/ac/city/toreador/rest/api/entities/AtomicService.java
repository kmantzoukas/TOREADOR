package uk.ac.city.toreador.rest.api.entities;

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
@Table(name = "atomicservices", catalog = "toreador")
public class AtomicService {
	
	@ApiModelProperty(required = false, example = "36")
	@Id
	@GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@ApiModelProperty(required=true, hidden = false)
	@OneToOne(optional = false, fetch=FetchType.EAGER)
	@JoinColumn(name = "cid")
	private Compositeservice compositeService;
	
	@ApiModelProperty(example = "name", required = true)
	@Column(name="name", nullable=false)
	private String name;
	
	@ApiModelProperty(hidden = true, example = "", required = false)
	@Column(name="owl", nullable=true)
	private byte[] owl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Compositeservice getCompositeService() {
		return compositeService;
	}

	public void setCompositeService(Compositeservice compositeService) {
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
	
}
