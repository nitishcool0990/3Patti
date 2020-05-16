package Gt.user.hibernateMapping;

import java.sql.Timestamp;

/**
 * GtGameAccounts entity. @author MyEclipse Persistence Tools
 */

public class GtGameAccounts implements java.io.Serializable {

	// Fields

	private Long id;
	private Integer userId;
	private String roomName;
	private Double chips;
	private Double chipsInPlay;
	private String status;
	private Timestamp modifiedDate;

	// Constructors

	/** default constructor */
	public GtGameAccounts() {
	}

	/** minimal constructor */
	public GtGameAccounts(Long id) {
		this.id = id;
	}

	/** full constructor */
	public GtGameAccounts(Long id, Integer userId, String roomName, double chips, double chipsInPlay, String status, Timestamp modifiedDate) {
		this.id = id;
		this.userId = userId;
		this.roomName = roomName;
		this.chips = chips;
		this.chipsInPlay = chipsInPlay;
		this.status = status;
		this.modifiedDate = modifiedDate;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRoomName() {
		return this.roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Double getChips() {
		return this.chips;
	}

	public void setChips(Double chips) {
		this.chips = chips;
	}

	public Double getChipsInPlay() {
		return this.chipsInPlay;
	}

	public void setChipsInPlay(Double chipsInPlay) {
		this.chipsInPlay = chipsInPlay;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		if(this!=null){
		return "GtGameAccounts [id=" + id + ", userId=" + userId + ", roomName=" + roomName + ", chips=" + chips
				+ ", chipsInPlay=" + chipsInPlay + ", status=" + status + "]";
	
		}else{
			return "";
		}
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
		
	
