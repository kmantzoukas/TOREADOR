package uk.ac.city.toreador.rest.api.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@Entity
@Table(name = "validationassets", catalog = "toreador")
public class ValidationAsset {

	@ApiModelProperty(required = false, example = "12")
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ApiModelProperty(required = true, hidden = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	private ValidationProject project;

	@ApiModelProperty(example = "name", required = true)
	@Column(name = "name", nullable = false)
	private String name;

	@ApiModelProperty(example = "OPERATION", allowableValues = "OPERATION,INPUT,OUTPUT")
	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private AssetType type;

	@ApiModelProperty(example = "")
	@Column(name = "rate", nullable = true)
	private Float rate;

	@ApiModelProperty(example = "HOUR", allowableValues = "SECOND,MINUTE,HOUR,DAY,WEEK")
	@Column(name = "timeunit", nullable = true)
	@Enumerated(EnumType.STRING)
	private TimeUnit timeunit;
	
	@ApiModelProperty(example = "")
	@Column(name = "securityproperty", nullable = true)
	private String securityproperty;
	
	@ApiModelProperty(required=false, hidden = false)
	@OneToMany(fetch=FetchType.EAGER, mappedBy="asset")
	private Set<GuardedAction> guardedAction;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@JsonIgnore
	public ValidationProject getProject() {
		return project;
	}
	
	public void setProject(ValidationProject project) {
		this.project = project;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AssetType getType() {
		return type;
	}

	public void setType(AssetType type) {
		this.type = type;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

	public TimeUnit getTimeunit() {
		return timeunit;
	}

	public void setTimeunit(TimeUnit timeunit) {
		this.timeunit = timeunit;
	}

	public String getSecurityproperty() {
		return securityproperty;
	}

	public void setSecurityproperty(String securityproperty) {
		this.securityproperty = securityproperty;
	}

	public Set<GuardedAction> getGuardedAction() {
		return guardedAction;
	}

	public void setGuardedAction(Set<GuardedAction> guardedAction) {
		this.guardedAction = guardedAction;
	}
	
}
