package Gt.HibernateMapping;

/**
 * AbstractGtGameTable entity provides the base persistence definition of the
 * GtGameTable entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractGtGameTable implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer gameConfigId;
	private String tableName;
	private String tableStatus;

	// Constructors

	/** default constructor */
	public AbstractGtGameTable() {
	}

	/** minimal constructor */
	public AbstractGtGameTable(Integer gameConfigId, String tableStatus) {
		this.gameConfigId = gameConfigId;
		this.tableStatus = tableStatus;
	}

	/** full constructor */
	public AbstractGtGameTable(Integer gameConfigId, String tableName, String tableStatus) {
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

}