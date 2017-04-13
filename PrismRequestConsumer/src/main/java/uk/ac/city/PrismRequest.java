package uk.ac.city;

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

@Entity
@Table(name = "prism_requests", catalog = "toreador")
public class PrismRequest implements Serializable{
	
	private static final long serialVersionUID = -5757552134175485875L;
	
	@Id
	@GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	
	@Column(name="model", nullable=false)
	private String model;
	
	@Column(name="properties", nullable=false)
	private String properties;
	
	@Column(name="output", nullable=false)
	private String output;
	
	@Column(name="created", nullable=false)
	private Timestamp created;
	
	@Column(name="status", nullable=false)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@ManyToOne(optional = false, fetch=FetchType.EAGER)
	@JoinColumn(name = "user")
	private User user;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	@Override
	public String toString() {
		return String
				.format("PrismRequest [id=%s, output=%s, created=%s, status=%s, user=%s]",
						id, output, created, status, user.getId());
	}
}