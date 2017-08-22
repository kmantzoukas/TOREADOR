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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "compositeservices", catalog = "toreador")
public class CompositeService implements java.io.Serializable {

	private Integer id;
	private Project project;
	private String name;
	private byte[] owls;
	private Set<AtomicService> atomicServices = new HashSet<AtomicService>(0);

	public CompositeService() {
	}

	public CompositeService(Project project, String name, byte[] owls, Set<AtomicService> atomicServices) {
		this.project = project;
		this.name = name;
		this.owls = owls;
		this.atomicServices = atomicServices;
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
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Column(name = "name", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonIgnore
	@Column(name = "owls")
	public byte[] getOwls() {
		return this.owls;
	}

	public void setOwls(byte[] owls) {
		this.owls = owls;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "compositeService")
	public Set<AtomicService> getAtomicServices() {
		return this.atomicServices;
	}

	public void setAtomicServices(Set<AtomicService> atomicServices) {
		this.atomicServices = atomicServices;
	}

}
