package com.rest.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "prism_requests", catalog = "toreador")
public class PrismRequest implements Serializable{
	
	private static final long serialVersionUID = -5757552134175485875L;
	
	@Id
	@GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	@Column(name="output_file", nullable=false)
	private String output;
	@Column(name="created", nullable=false)
	private Timestamp created;
	@Column(name="status", nullable=false)
	private String status;
	
	@ManyToOne(optional = false, fetch=FetchType.EAGER)
	@JoinColumn(name = "user")
	@JsonIgnore
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}