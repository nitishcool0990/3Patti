package Gt.user.hibernateMapping;

import java.sql.Timestamp;

/**
 * GtTranscationHistory entity. @author MyEclipse Persistence Tools
 */

public class GtTranscationHistory implements java.io.Serializable {

	// Fields

	private Long id;
	private Integer userId;
	private String chipType;
	private Integer gameTableId;
	private double chips;
	private String crDr;
	private String remarks;
	private Timestamp modifiedDate;

	// Constructors

	/** default constructor */
	public GtTranscationHistory() {
	}

	/** full constructor */
	public GtTranscationHistory(Integer userId, String chipType, Integer gameTableId, Integer chips, String crDr,
			String remarks, Timestamp modifiedDate) {
		this.userId = userId;
		this.chipType = chipType;
		this.gameTableId = gameTableId;
		this.chips = chips;
		this.crDr = crDr;
		this.remarks = remarks;
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

	public String getChipType() {
		return this.chipType;
	}

	public void setChipType(String chipType) {
		this.chipType = chipType;
	}

	public Integer getGameTableId() {
		return this.gameTableId;
	}

	public void setGameTableId(Integer gameTableId) {
		this.gameTableId = gameTableId;
	}

	public double getChips() {
		return this.chips;
	}

	public void setChips(double chips) {
		this.chips = chips;
	}

	public String getCrDr() {
		return this.crDr;
	}

	public void setCrDr(String crDr) {
		this.crDr = crDr;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		
		this.modifiedDate = modifiedDate ;
	}

}