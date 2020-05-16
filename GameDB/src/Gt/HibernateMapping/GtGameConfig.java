package Gt.HibernateMapping;

import java.sql.Timestamp;
import java.util.Date;

/**
 * GtGameConfig entity. @author MyEclipse Persistence Tools
 */

public class GtGameConfig implements java.io.Serializable {

	// Fields

	private Integer gameConfigId;
	private String chipType;
	private String gameVariant;
	private Integer minPlayers;
	private Integer maxPlayers;
	private Integer minRoom;
	private Integer maxRoom;
	private Integer isPrivate;
	private String status;
	private Timestamp modifiedTime;
	private Double minBuyIn;
	private Double maxBuyIn;
	private String remark;

	// Constructors

	/** default constructor */
	public GtGameConfig() {
		
	}

	/** minimal constructor */
	public GtGameConfig(String gameVariant, Integer minPlayers, Integer maxPlayers, String status) {
		this.gameVariant = gameVariant;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.status = status;
	}

	/** full constructor */
	public GtGameConfig(String chipType, String gameVariant, Integer minPlayers, Integer maxPlayers, Integer minRoom,
			Integer maxRoom, Integer isPrivate, String status, Timestamp modifiedTime, String remark) {
		this.chipType = chipType;
		this.gameVariant = gameVariant;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.minRoom = minRoom;
		this.maxRoom = maxRoom;
		this.isPrivate = isPrivate;
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

	public String getChipType() {
		return this.chipType;
	}

	public void setChipType(String chipType) {
		this.chipType = chipType;
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

	public Integer getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Integer isPrivate) {
		this.isPrivate = isPrivate;
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
		 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	   

		this.modifiedTime = timestamp;
	}

	public Double getMinBuyIn() {
		return minBuyIn;
	}

	public void setMinBuyIn(Double minBuyIn) {
		this.minBuyIn = minBuyIn;
	}

	public Double getMaxBuyIn() {
		return maxBuyIn;
	}

	public void setMaxBuyIn(Double maxBuyIn) {
		this.maxBuyIn = maxBuyIn;
	}

	@Override
	public String toString() {
		return "GtGameConfig [gameConfigId=" + gameConfigId + ", chipType=" + chipType + ", gameVariant=" + gameVariant
				+ ", minPlayers=" + minPlayers + ", maxPlayers=" + maxPlayers + ", minRoom=" + minRoom + ", maxRoom="
				+ maxRoom + ", status=" + status + ", modifiedTime=" + modifiedTime + ", minBuyIn=" + minBuyIn
				+ ", maxBuyIn=" + maxBuyIn + "]";
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}