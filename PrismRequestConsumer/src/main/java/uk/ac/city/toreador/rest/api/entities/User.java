package uk.ac.city.toreador.rest.api.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users", catalog = "toreador")
public class User implements java.io.Serializable {

	private Integer id;
	private String name;
	private String surname;
	private String username;
	private String password;
	private Set<Project> projects = new HashSet<Project>(0);

	public User() {
	}

	public User(String name, String surname, String username, String password) {
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password = password;
	}

	public User(String name, String surname, String username, String password, Set<Project> projects) {
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password = password;
		this.projects = projects;
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

	@Column(name = "name", nullable = false, length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "surname", nullable = false, length = 45)
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Column(name = "username", nullable = false, length = 45)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false, length = 45)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

}
