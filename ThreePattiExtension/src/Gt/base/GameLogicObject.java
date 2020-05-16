package Gt.base;

import java.util.ArrayList;
import java.util.List;

import Gt.common.Card;
import Gt.room.Logic.Ak47GameLogic;
import Gt.room.Logic.MuflisGameLogic;
import Gt.room.Logic.SimpleGameLogic;

public class GameLogicObject {
	public static final String MUFLIS = "muflis";
	public static final String AK47 = "ak47";
	public static final String JOKER = "joker";
	public static final String NORMAL = "normal";
	
	public List<Card> jokers = new ArrayList<Card>();
	
	
	public GameLogicObject() {
		
	}

	public GameLogicObject(List<Card> jokers) {
		this.jokers = jokers;
	}

	public BaseGameLogic getGameLogicObj(String value){
		BaseGameLogic logic = null;
		if(value.equals(MUFLIS)){
			logic = new MuflisGameLogic();
		}else if(value.equals(AK47)){
			logic = new Ak47GameLogic();
		}
		else if( value.equals(JOKER)){
			logic = new Ak47GameLogic(this.jokers);
		}else {
			logic = new SimpleGameLogic();
		}
		return logic;
		
	}
	
}
