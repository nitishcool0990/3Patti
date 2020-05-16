package HibernateMapping;

public class GtGameLogGameActivity {
	
	private Long id;
	private Long tableGameId;
	private Long gameId;
	private String playerAction;
	private Long playerId;
	private String handCards;
	private String status;
	private Double chipsLeft;
	private String systemActions;
	private Long playerActionAmount;
	private String remark;
	private Double potAmount;
	private String actionTime;
	private Integer rakePercent;
	
	public GtGameLogGameActivity() {
		
	}

	public GtGameLogGameActivity(Long id, Long tableGameId, Long gameId, String playerAction, Long playerId,
			String handCards, String status, Double chipsLeft, String systemActions, Long playerActionAmount,
			String remark) {
		super();
		this.id = id;
		this.tableGameId = tableGameId;
		this.gameId = gameId;
		this.playerAction = playerAction;
		this.playerId = playerId;
		this.handCards = handCards;
		this.status = status;
		this.chipsLeft = chipsLeft;
		this.systemActions = systemActions;
		this.playerActionAmount = playerActionAmount;
		this.remark = remark;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTableGameId() {
		return tableGameId;
	}

	public void setTableGameId(Long tableGameId) {
		this.tableGameId = tableGameId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public String getPlayerAction() {
		return playerAction;
	}

	public void setPlayerAction(String playerAction) {
		this.playerAction = playerAction;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public String getHandCards() {
		return handCards;
	}

	public void setHandCards(String handCards) {
		this.handCards = handCards;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getChipsLeft() {
		return chipsLeft;
	}

	public void setChipsLeft(Double chipsLeft) {
		this.chipsLeft = chipsLeft;
	}

	public String getSystemActions() {
		return systemActions;
	}

	public void setSystemActions(String systemActions) {
		this.systemActions = systemActions;
	}

	public Long getPlayerActionAmount() {
		return playerActionAmount;
	}

	public void setPlayerActionAmount(Long playerActionAmount) {
		this.playerActionAmount = playerActionAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Double getPotAmount() {
		return potAmount;
	}

	public void setPotAmount(Double potAmount) {
		this.potAmount = potAmount;
	}

	public String getActionTime() {
		return actionTime;
	}

	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}

	@Override
	public String toString() {
		return "GtGameLogGameActivity [id=" + id + ", tableGameId=" + tableGameId + ", gameId=" + gameId
				+ ", playerAction=" + playerAction + ", playerId=" + playerId + ", handCards=" + handCards + ", status="
				+ status + ", chipsLeft=" + chipsLeft + ", systemActions=" + systemActions + ", playerActionAmount="
				+ playerActionAmount + ", remark=" + remark + "]";
	}

	public Integer getRakePercent() {
		return rakePercent;
	}

	public void setRakePercent(Integer rakePercent) {
		this.rakePercent = rakePercent;
	}
	
	
}

