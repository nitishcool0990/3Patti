package HibernateMapping;

import java.sql.Timestamp;

public class GtGameLogStartEnd {
	
	private Long id;
	private Long tableId;
	private Long gameId;
	private int noOfActivePlayers;
	private String chipType;
	private String gameVariant;
	private String mixVariant;
	private Long buyLow;
	private Long buyHigh;
	private Long ante;
	private Long dealerId;
	private String modifiedAt;
	private Timestamp createdAt;
	
	public GtGameLogStartEnd() {

	}
	
	public GtGameLogStartEnd(Long id, Long tableId, Long gameId, int noOfActivePlayers, String chipType,
			String gameVariant, String mixVariant, Long buyLow, Long buyHigh, Long ante, Long dealerId,
			String modifiedAt, Timestamp createdAt) {
		this.id = id;
		this.tableId = tableId;
		this.gameId = gameId;
		this.noOfActivePlayers = noOfActivePlayers;
		this.chipType = chipType;
		this.gameVariant = gameVariant;
		this.mixVariant = mixVariant;
		this.buyLow = buyLow;
		this.buyHigh = buyHigh;
		this.ante = ante;
		this.dealerId = dealerId;
		this.modifiedAt = modifiedAt;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public int getNoOfActivePlayers() {
		return noOfActivePlayers;
	}

	public void setNoOfActivePlayers(int noOfActivePlayers) {
		this.noOfActivePlayers = noOfActivePlayers;
	}

	public String getChipType() {
		return chipType;
	}

	public void setChipType(String chipType) {
		this.chipType = chipType;
	}

	public String getGameVariant() {
		return gameVariant;
	}

	public void setGameVariant(String gameVariant) {
		this.gameVariant = gameVariant;
	}

	public String getMixVariant() {
		return mixVariant;
	}

	public void setMixVariant(String mixVariant) {
		this.mixVariant = mixVariant;
	}

	public Long getBuyLow() {
		return buyLow;
	}

	public void setBuyLow(Long buyLow) {
		this.buyLow = buyLow;
	}

	public Long getBuyHigh() {
		return buyHigh;
	}

	public void setBuyHigh(Long buyHigh) {
		this.buyHigh = buyHigh;
	}

	public Long getAnte() {
		return ante;
	}

	public void setAnte(Long ante) {
		this.ante = ante;
	}

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public String getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "GtGameLogStartEnd [id=" + id + ", tableId=" + tableId + ", gameId=" + gameId + ", noOfActivePlayers="
				+ noOfActivePlayers + ", chipType=" + chipType + ", gameVariant=" + gameVariant + ", mixVariant="
				+ mixVariant + ", buyLow=" + buyLow + ", buyHigh=" + buyHigh + ", ante=" + ante + ", dealerId="
				+ dealerId + ", modifiedAt=" + modifiedAt + ", createdAt=" + createdAt + "]";
	}
	
	

}
