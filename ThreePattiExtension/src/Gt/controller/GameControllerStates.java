package Gt.controller;

public enum GameControllerStates {
	IDLE("idle"),
	GAME_START("game_start"),
	GAME_STARTED("game_started"),
	ANTE("ante"),
	INIT_BETTING("init_betting"),
	BETTING("betting"),
	SHOW("show"),
	SENDROUNDEND("round_end"),
	DEALER_RESPONSE("dealer_response");
	
	private String state;
	
	GameControllerStates(String state){
		this.state = state;
	}
	
	public String toString(){
		return this.state;
	}
	
	
	
}
