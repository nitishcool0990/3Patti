package HibernateMapping;

public class GtGameLogPlayerJoined {
	private Long id;
	private Long gameId;
	private Long tableGameId;
	private Long playerId;
	private Long seatId;
	private Double gameInitialChips;
	
	public GtGameLogPlayerJoined() {

	}

	public GtGameLogPlayerJoined(Long id, Long gameId, Long tableGameId, Long playerId, Long seatId,
			Double gameInitialChips) {
		super();
		this.id = id;
		this.gameId = gameId;
		this.tableGameId = tableGameId;
		this.playerId = playerId;
		this.seatId = seatId;
		this.gameInitialChips = gameInitialChips;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Long getTableGameId() {
		return tableGameId;
	}

	public void setTableGameId(Long tableGameId) {
		this.tableGameId = tableGameId;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public Long getSeatId() {
		return seatId;
	}

	public void setSeatId(Long seatId) {
		this.seatId = seatId;
	}

	public Double getGameInitialChips() {
		return gameInitialChips;
	}

	public void setGameInitialChips(Double gameInitialChips) {
		this.gameInitialChips = gameInitialChips;
	}

	@Override
	public String toString() {
		return "GtGameLogPlayerJoined [id=" + id + ", gameId=" + gameId + ", tableGameId=" + tableGameId + ", playerId="
				+ playerId + ", seatId=" + seatId + ", gameInitialChips=" + gameInitialChips + "]";
	}
	
	
	
}
