package uk.ac.city.toreador.rest.api.entities;


import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "projects", catalog = "toreador")
public class Project implements java.io.Serializable {

	private Integer id;
	private User user;
	private String name;
	private byte[] model;
	private byte[] properties;
	private byte[] validationoutput;
	private Date created;
	private String status;
	private String propertyCategoryCatalog;
	private byte[] wsagreement;
	private byte[] monitoringoutput;
	private Set<Asset> assets = new HashSet<Asset>(0);
	private Set<CompositeService> compositeservices = new HashSet<CompositeService>(0);

	public Project() {
	}

	public Project(User user, String name, Date created, String status, String propertyCategoryCatalog) {
		this.user = user;
		this.name = name;
		this.created = created;
		this.status = status;
		this.propertyCategoryCatalog = propertyCategoryCatalog;
	}

	public Project(User users, String name, byte[] model, byte[] properties, byte[] validationoutput, Date created,
			String status, String propertyCategoryCatalog, byte[] wsagreement, byte[] monitoringoutput,
			Set<Asset> assetses, Set<CompositeService> compositeserviceses) {
		this.user = users;
		this.name = name;
		this.model = model;
		this.properties = properties;
		this.validationoutput = validationoutput;
		this.created = created;
		this.status = status;
		this.propertyCategoryCatalog = propertyCategoryCatalog;
		this.wsagreement = wsagreement;
		this.monitoringoutput = monitoringoutput;
		this.assets = assetses;
		this.compositeservices = compositeserviceses;
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
	@JoinColumn(name = "userId", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "name", nullable = false, length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "model")
	public byte[] getModel() {
		return this.model;
	}

	public void setModel(byte[] model) {
		this.model = model;
	}
	
	@Column(name = "properties")
	public byte[] getProperties() {
		return this.properties;
	}

	public void setProperties(byte[] properties) {
		this.properties = properties;
	}
	
	@Column(name = "validationoutput")
	public byte[] getValidationoutput() {
		return this.validationoutput;
	}

	public void setValidationoutput(byte[] validationoutput) {
		this.validationoutput = validationoutput;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false, length = 19)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name = "status", nullable = false, length = 10)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "propertyCategoryCatalog", nullable = false, length = 5)
	public String getPropertyCategoryCatalog() {
		return this.propertyCategoryCatalog;
	}

	public void setPropertyCategoryCatalog(String propertyCategoryCatalog) {
		this.propertyCategoryCatalog = propertyCategoryCatalog;
	}
	
	@Column(name = "wsagreement")
	public byte[] getWsagreement() {
		return this.wsagreement;
	}

	public void setWsagreement(byte[] wsagreement) {
		this.wsagreement = wsagreement;
	}
	
	@Column(name = "monitoringoutput")
	public byte[] getMonitoringoutput() {
		return this.monitoringoutput;
	}

	public void setMonitoringoutput(byte[] monitoringoutput) {
		this.monitoringoutput = monitoringoutput;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "project")
	public Set<Asset> getAssets() {
		return this.assets;
	}

	public void setAssets(Set<Asset> assets) {
		this.assets = assets;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "project")
	public Set<CompositeService> getCompositeservices() {
		return this.compositeservices;
	}

	public void setCompositeservices(Set<CompositeService> compositeservices) {
		this.compositeservices = compositeservices;
	}

}
