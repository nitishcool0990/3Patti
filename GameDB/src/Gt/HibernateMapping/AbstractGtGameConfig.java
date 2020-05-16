package Gt.HibernateMapping;

import java.sql.Timestamp;

/**
 * AbstractGtGameConfig entity provides the base persistence definition of the
 * GtGameConfig entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractGtGameConfig implements java.io.Serializable {

	// Fields
	
	private Integer gameConfigId;
	private String gameVariant;
	private Integer minPlayers;
	private Integer maxPlayers;
	private Integer minRoom;
	private Integer maxRoom;
	private String status;
	private Timestamp modifiedTime;
	private String remark;

	// Constructors

	/** default constructor */
	public AbstractGtGameConfig() {
	}

	/** minimal constructor */
	public AbstractGtGameConfig(String gameVariant, Integer minPlayers, Integer maxPlayers, String status) {
		this.gameVariant = gameVariant;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.status = status;
	}

	/** full constructor */
	public AbstractGtGameConfig(String gameVariant, Integer minPlayers, Integer maxPlayers, Integer minRoom,
			Integer maxRoom, String status, Timestamp modifiedTime,String remark) {
		this.gameVariant = gameVariant;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.minRoom = minRoom;
		this.maxRoom = maxRoom;
		this.status = status;
		this.modifiedTime = modifiedTime;
		this.remark = remark;
	}

	// Property accessors

	public Integer getGameConfigId() {
		return this.gameConfigId;
	}

	public void setGameConfigId(Integer gameConfigId) {
		this.gameConfigId = gameConfigId;
	}

	public String getGameVariant() {
		return this.gameVariant;
	}

	public void setGameVariant(String gameVariant) {
		this.gameVariant = gameVariant;
	}

	public Integer getMinPlayers() {
		return this.minPlayers;
	}

	public void setMinPlayers(Integer minPlayers) {
		this.minPlayers = minPlayers;
	}

	public Integer getMaxPlayers() {
		return this.maxPlayers;
	}

	public void setMaxPlayers(Integer maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public Integer getMinRoom() {
		return this.minRoom;
	}

	public void setMinRoom(Integer minRoom) {
		this.minRoom = minRoom;
	}

	public Integer getMaxRoom() {
		return this.maxRoom;
	}

	public void setMaxRoom(Integer maxRoom) {
		this.maxRoom = maxRoom;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getModifiedTime() {
		return this.modifiedTime;
	}

	public void setModifiedTime(Timestamp modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}