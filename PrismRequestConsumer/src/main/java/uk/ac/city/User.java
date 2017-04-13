package uk.ac.city;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users", catalog = "toreador")
public class User implements Serializable{
	
	private static final long serialVersionUID = 7868533363937836866L;
	
	@Id
	@GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	private Long id;
	@Column(name="name", nullable=false)
	private String name;
	@Column(name="surname", nullable=false)
	private String surname;
	@Column(name="username", nullable=false)
	private String username;
	@Column(name="password", nullable=false)
	private String password;
	
	@OneToMany(mappedBy = "user", fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<PrismRequest> prismRequests = new ArrayList<PrismRequest>();
	
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
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<PrismRequest> getPrismRequests() {
		return prismRequests;
	}
	public void setPrismRequests(List<PrismRequest> prismRequests) {
		this.prismRequests = prismRequests;
	}
	@Override
	public String toString() {
		return String
				.format("User [id=%s, name=%s, surname=%s, username=%s, password=%s, prismRequests=%s]",
						id, name, surname, username, password, prismRequests);
	}
}
