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
@Table(name = "securityproperties", catalog = "toreador")
public class SecurityProperty implements java.io.Serializable {

	private Integer id;
	private String name;
	private Set<Slotemplate> slotemplates = new HashSet<Slotemplate>(0);
	private Set<AssetSecurityPropertyPair> assetSecuritypropertyPairs = new HashSet<AssetSecurityPropertyPair>(0);

	public SecurityProperty() {
	}

	public SecurityProperty(String name, Set<Slotemplate> slotemplates) {
		this.name = name;
		this.slotemplates = slotemplates;
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

	@Column(name = "name", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "securityProperty")
	public Set<Slotemplate> getSlotemplates() {
		return this.slotemplates;
	}

	public void setSlotemplates(Set<Slotemplate> slotemplates) {
		this.slotemplates = slotemplates;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "securityProperty")
	public Set<AssetSecurityPropertyPair> getAssetSecuritypropertyPairs() {
		return this.assetSecuritypropertyPairs;
	}

	public void setAssetSecuritypropertyPairs(Set<AssetSecurityPropertyPair> assetSecuritypropertyPairs) {
		this.assetSecuritypropertyPairs = assetSecuritypropertyPairs;
	}

}
