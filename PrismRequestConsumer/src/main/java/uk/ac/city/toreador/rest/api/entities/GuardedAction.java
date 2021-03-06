package uk.ac.city.toreador.rest.api.entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "guardedactions", catalog = "toreador")
public class GuardedAction implements java.io.Serializable {

	private Integer id;
	private AssetSecurityPropertyPair assetSecurityPropertyPair;
	private String action;
	private Integer penalty;
	private String guard;
	private String name;

	public GuardedAction() {
	}

	public GuardedAction(AssetSecurityPropertyPair assetSecurityPropertyPair, String action, Integer penalty, String guard, String name) {
		this.assetSecurityPropertyPair = assetSecurityPropertyPair;
		this.action = action;
		this.penalty = penalty;
		this.guard = guard;
		this.name = name;
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
	@JoinColumns({
		  @JoinColumn(name = "aid", insertable = true, updatable = false),
		  @JoinColumn(name = "spid", insertable = true, updatable = false)
		})
	public AssetSecurityPropertyPair getAssetSecurityPropertyPair() {
		return this.assetSecurityPropertyPair;
	}
	
	public void setAssetSecurityPropertyPair(AssetSecurityPropertyPair assetSecurityPropertyPair) {
		this.assetSecurityPropertyPair = assetSecurityPropertyPair;
	}

	@Column(name = "action", length = 11)
	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Column(name = "penalty")
	public Integer getPenalty() {
		return this.penalty;
	}

	public void setPenalty(Integer penalty) {
		this.penalty = penalty;
	}

	@Column(name = "guard", length = 65535)
	public String getGuard() {
		return this.guard;
	}

	public void setGuard(String guard) {
		this.guard = guard;
	}

	@Column(name = "name", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
