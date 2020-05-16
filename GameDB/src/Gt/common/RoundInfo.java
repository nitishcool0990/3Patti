package Gt.common;

import java.util.ArrayList;
import java.util.List;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class RoundInfo {
	private List<PlayerRoundInfo> playerRoundInfoList = new ArrayList<PlayerRoundInfo>();
	private int gameId = 0;
	private int tableId = 0;
	private String gameVariant = "";
	
	public List<PlayerRoundInfo> getPlayerRoundInfoList() {
		return playerRoundInfoList;
	}
	public void setPlayerHisoryList(List<PlayerRoundInfo> playerRoundInfoList) {
		this.playerRoundInfoList = playerRoundInfoList;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public int getTableId() {
		return tableId;
	}
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	public String getGameVariant() {
		return gameVariant;
	}
	public void setGameVariant(String gameVariant) {
		this.gameVariant = gameVariant;
	}
	public ISFSObject toSFSObject()
	  {
	    ISFSObject sfso = new SFSObject();
	    sfso.putClass("roundInfo", this);
	    return sfso;
	  }
	@Override
	public String toString() {
		return "RoundInfo [playerRoundInfoList=" + playerRoundInfoList + ", gameId=" + gameId + ", tableId=" + tableId
				+ ", gameVariant=" + gameVariant + "]";
	}
	
	
}
