package uk.ac.city.toreador.rest.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@Entity
@Table(name = "validationassets", catalog = "toreador")
public class ValidationAsset {

	@ApiModelProperty(required = false, example = "36")
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ApiModelProperty(required = true, hidden = false)
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "pid")
	private ValidationProject project;

	@ApiModelProperty(example = "name", required = true)
	@Column(name = "name", nullable = false)
	private String name;

	@ApiModelProperty(example = "OPERATION", allowableValues = "OPERATION,DATA")
	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private AssetType type;

	@ApiModelProperty(example = "")
	@Column(name = "rate", nullable = true)
	private String rate;

	@ApiModelProperty(example = "HOUR", allowableValues = "SECOND,MINUTE,HOUR,DAY,WEEK")
	@Column(name = "timeunit", nullable = true)
	@Enumerated(EnumType.STRING)
	private TimeUnit timeunit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public TimeUnit getTimeunit() {
		return timeunit;
	}

	public void setTimeunit(TimeUnit timeunit) {
		this.timeunit = timeunit;
	}

}
