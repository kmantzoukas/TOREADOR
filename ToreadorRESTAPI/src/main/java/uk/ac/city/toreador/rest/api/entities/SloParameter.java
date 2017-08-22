package uk.ac.city.toreador.rest.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sloparameters", catalog = "toreador")
public class SloParameter implements java.io.Serializable {

	private int id;
	private Slotemplate sloTemplate;
	private String name;

	public SloParameter() {
	}

	public SloParameter(int id) {
		this.id = id;
	}

	public SloParameter(int id, Slotemplate sloTemplate, String name) {
		this.id = id;
		this.sloTemplate = sloTemplate;
		this.name = name;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "slotid")
	public Slotemplate getSloTemplate() {
		return this.sloTemplate;
	}

	public void setSloTemplate(Slotemplate sloTemplate) {
		this.sloTemplate = sloTemplate;
	}

	@Column(name = "name", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
