package uk.ac.city.toreador.rest.api.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
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

@Entity
@Table(name = "atomicservices", catalog = "toreador")
public class AtomicService implements java.io.Serializable {

	private Integer id;
	private CompositeService compositeService;
	private String name;
	private byte[] owl;
	private Set<Operation> operations = new HashSet<Operation>(0);

	public AtomicService() {
	}

	public AtomicService(CompositeService compositeService, String name, byte[] owl, Set<Operation> operations) {
		this.compositeService = compositeService;
		this.name = name;
		this.owl = owl;
		this.operations = operations;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cid")
	public CompositeService getCompositeService() {
		return this.compositeService;
	}

	public void setCompositeService(CompositeService compositeService) {
		this.compositeService = compositeService;
	}

	@Column(name = "name", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "owl")
	public byte[] getOwl() {
		return this.owl;
	}

	public void setOwl(byte[] owl) {
		this.owl = owl;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "atomicService")
	public Set<Operation> getOperations() {
		return this.operations;
	}

	public void setOperations(Set<Operation> operations) {
		this.operations = operations;
	}

}
