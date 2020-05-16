package Gt.HibernateMapping;

/**
 * GtGamePropId entity. @author MyEclipse Persistence Tools
 */

public class GtGamePropId implements java.io.Serializable {

	// Fields

	private Long id;
	private Integer gameConfigId;

	// Constructors

	/** default constructor */
	public GtGamePropId() {
	}

	/** full constructor */
	public GtGamePropId(Long id, Integer gameConfigId) {
		this.id = id;
		this.gameConfigId = gameConfigId;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getGameConfigId() {
		return this.gameConfigId;
	}

	public void setGameConfigId(Integer gameConfigId) {
		this.gameConfigId = gameConfigId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof GtGamePropId))
			return false;
		GtGamePropId castOther = (GtGamePropId) other;

		return ((this.getId() == castOther.getId())
				|| (this.getId() != null && castOther.getId() != null && this.getId().equals(castOther.getId())))
				&& ((this.getGameConfigId() == castOther.getGameConfigId())
						|| (this.getGameConfigId() != null && castOther.getGameConfigId() != null
								&& this.getGameConfigId().equals(castOther.getGameConfigId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result + (getGameConfigId() == null ? 0 : this.getGameConfigId().hashCode());
		return result;
	}

}