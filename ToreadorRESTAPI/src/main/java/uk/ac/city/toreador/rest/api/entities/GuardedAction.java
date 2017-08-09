package uk.ac.city.toreador.rest.api.entities;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@Entity
@Table(name = "guardedactions", catalog = "toreador")
public class GuardedAction {

	@ApiModelProperty(required = false, example = "10")
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ApiModelProperty(required = true, hidden = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "aid")
	private ValidationAsset asset;

	@ApiModelProperty(example = "RENEGOTIATE", allowableValues = "PENALTY,RENEGOTIATE,OTHER")
	@Column(name="action", nullable=false)
	@Enumerated(EnumType.STRING)
	private GuardedActionType action;

	@ApiModelProperty(example = "")
	@Column(name = "penalty", nullable = true)
	private Float penalty;

	@ApiModelProperty(example = "")
	@Column(name = "guard", nullable = true)
	private String guard;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@JsonIgnore
	public ValidationAsset getAsset() {
		return asset;
	}

	public void setAsset(ValidationAsset asset) {
		this.asset = asset;
	}

	public GuardedActionType getAction() {
		return action;
	}

	public void setAction(GuardedActionType action) {
		this.action = action;
	}

	public Float getPenalty() {
		return penalty;
	}

	public void setPenalty(Float penalty) {
		this.penalty = penalty;
	}

	public String getGuard() {
		return guard;
	}

	public void setGuard(String guard) {
		this.guard = guard;
	}
	
}
