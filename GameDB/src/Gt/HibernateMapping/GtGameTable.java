package Gt.HibernateMapping;

/**
 * GtGameTable entity. @author MyEclipse Persistence Tools
 */

public class GtGameTable implements java.io.Serializable {

	// Fields
	private Integer id;
	private Integer gameConfigId;
	private String tableName;
	private String tableStatus;

	// Constructors

	/** default constructor */
	public GtGameTable() {
	}

	/** minimal constructor */
	public GtGameTable(Integer gameConfigId, String tableStatus) {
		this.gameConfigId = gameConfigId;
		this.tableStatus = tableStatus;
	}

	/** full constructor */
	public GtGameTable(Integer gameConfigId, String tableName, String tableStatus) {
		this.gameConfigId = gameConfigId;
		this.tableName = tableName;
		this.tableStatus = tableStatus;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGameConfigId() {
		return this.gameConfigId;
	}

	public void setGameConfigId(Integer gameConfigId) {
		this.gameConfigId = gameConfigId;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableStatus() {
		return this.tableStatus;
	}

	public void setTableStatus(String tableStatus) {
		this.tableStatus = tableStatus;
	}

	@Override
	public String toString() {
		return "GtGameTable [id=" + id + ", gameConfigId=" + gameConfigId + ", tableName=" + tableName
				+ ", tableStatus=" + tableStatus + "]";
	}
}