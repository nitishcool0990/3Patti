package Gt.HibernateMapping;

/**
 * GtGameProp entity. @author MyEclipse Persistence Tools
 */

public class GtGameProp implements java.io.Serializable {

	// Fields

	private Long id;
	private Integer gameConfigId;
	private String propName;
	private String propValue;
	private String propType;

	// Constructors

	/** default constructor */
	public GtGameProp() {
	}

	/** minimal constructor */
	public GtGameProp(Integer gameConfigId) {
		this.gameConfigId = gameConfigId;
	}

	/** full constructor */
	public GtGameProp(Integer gameConfigId, String propName, String propValue, String propType) {
		this.gameConfigId = gameConfigId;
		this.propName = propName;
		this.propValue = propValue;
		this.propType = propType;
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

	public String getPropName() {
		return this.propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getPropValue() {
		return this.propValue;
	}

	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}

	public String getPropType() {
		return this.propType;
	}

	public void setPropType(String propType) {
		this.propType = propType;
	}

	@Override
	public String toString() {
		return "GtGameProp [id=" + id + ", gameConfigId=" + gameConfigId + ", propName=" + propName + ", propValue="
				+ propValue + ", propType=" + propType + "]";
	}
	
	

}