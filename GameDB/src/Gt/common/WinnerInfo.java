package Gt.common;

import java.util.ArrayList;
import java.util.List;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

public class WinnerInfo implements SerializableSFSType{
	public List<Winner> winnerList = new ArrayList<Winner> ();

	public List<Winner> getWinnerList() {
		return winnerList;
	}

	public void setWinnerList(List<Winner> winnerList) {
		this.winnerList = winnerList;
	}
	
	 public ISFSObject toSFSObject()
	  {
	    ISFSObject sfso = new SFSObject();
	    sfso.putClass("winInfo", this);
	    return sfso;
	  }

	@Override
	public String toString() {
		return "WinnerInfo [winnerList=" + winnerList + "]";
	}
	 
	 
	
}
