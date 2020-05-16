package Gt.common;

import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;
import java.util.ArrayList;
import java.util.List;

public class PlayersInfo
  implements SerializableSFSType
{
  public List<PlayerDetails> playerInfoList = new ArrayList<PlayerDetails>();
  
  public void setPlayerInfoList(List<PlayerDetails> playerInfoList)
  {
    this.playerInfoList = playerInfoList;
  }
  
  public List<PlayerDetails> getPlayerInfoList()
  {
    return this.playerInfoList;
  }
  
  public boolean equals(Object o)
  {
    try
    {
      PlayersInfo playersInfo = (PlayersInfo)o;
      if (this.playerInfoList.size() != playersInfo.playerInfoList.size()) {
        return false;
      }
      for (int i = 0; i < this.playerInfoList.size(); i++) {
        if (!((PlayerDetails)this.playerInfoList.get(i)).equals(playersInfo.getPlayerInfoList().get(i))) {
          return false;
        }
      }
      return true;
    }
    catch (Exception e) {
    	
    }
    return false;
  }
}

