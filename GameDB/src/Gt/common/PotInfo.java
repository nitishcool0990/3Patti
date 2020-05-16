package Gt.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

public class PotInfo implements SerializableSFSType {
	
	public List<Pot> pots = null;
	
	public List<Pot> getPots(){
		return this.pots;
	}
	
	public PotInfo() {
		pots = new ArrayList<Pot>();
	}
	
	public void addPot(double chips,  List<Integer>  playerIds){
		Pot pot = new Pot(chips,  playerIds);
		pots.add(pot);
	}
	
	
	public void updatePot(int playerId,double chipAmount) {
		boolean flag = false;
		Pot pot = this.pots.get(pots.size() - 1);
		double chipsInPot = pot.getChips();

		pot.setChips(chipsInPot + chipAmount);
		for(int playerTotalBets : pot.getPlayerIds()){
			if(playerTotalBets == playerId){
				flag =true;
				break;
			}
		}
		if(!flag){
			pot.getPlayerIds().add(playerId);
		}
		
	}
	
	 public ISFSObject toSFSObject()
	  {
	    ISFSObject sfso = new SFSObject();
	    sfso.putClass("potInfo", this);
	    return sfso;
	  }

	@Override
	public String toString() {
		return "PotInfo [pots = " + pots +" ]";
	}
	 
	 
	 
	 

}
