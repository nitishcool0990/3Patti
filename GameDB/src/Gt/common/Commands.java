package Gt.common;

public enum Commands {
	CMD_KEEPALIVE("lobby.keepAlive"),
	CMD_SEATINFO("game.seatInfo"),
	CMD_USERROOMLIST("game.userRoomList"),
	CMD_ROOMDATA("game.roomData"),
	CMD_BADCOMMAND("game.badCommand"),
	CMD_CLIENTREADY("game.clientready"),
	CMD_MASSAGE("game.msg"),
	CMD_DEALER("game.dealer"),
	CMD_LEAVESEAT("game.leaveSeat"),
	CMD_PLAYERCARDS("game.playerCards"),
	CMD_PLAYERTOOKACTION("game.playerTookAction"),
	
	CMD_SIDESHOWREQUEST("game.sideShowReq"),
	CMD_SIDESHOWRESPONSE("game.sideShowRes"), 
	CMD_SHOW("card.show"),
	CMD_WINNER("game.winner"), 
	CMD_SEE("game.see"), 
	CMD_PLAYERSEEN("game.playerSeen"),
	CMD_POTINFO("game.potInfo"),
	CMD_RESERVEDSEAT("game.reservedSeat"),
	CMD_ALLOW_TAKESEAT("game.allowedTakeSeat"),
	CMD_TAKESEAT("game.takeSeat"),
	
	 CMD_PLAYER_STATUS("game.playerStatus"),
	 CMD_BUYINDATA("game.buyInData"),
	 CMD_GAMEINFO("game.gameInfo"),
	 CMD_GAMESTART("game.gameStart"),
	 CMD_GAMESTARTED("game.gameStarted"),
	 CMD_WHOSETURN("game.whoseTurn"),
	 CMD_PLAYERCHOICES("game.playerChoices"),
	 CMD_DEALTCARD("game.dealtCard"),
	 CMD_ROUNDEND("game.roundEnd"),
	 CMD_SHOWCARDS("game.showCards"),
	 CMD_SHOWSTATE("game.showState"),
	 CMD_BETTINGSTATE("game.bettingState"), 
	 CMD_IDLE("game.idleState"), 
	 CMD_GAMEACCOUNT("lobby.myAccount"),
	 CMD_GAMEVARIANTS("game.variants"),
	 CMD_DEALERRESPONSE("game.dealerResponse"),
	 CMD_CLIENTEXCEPTION("lobby.clientException"),
	 CMD_PLAYERRANKS("game.playerRanks"),
	 CMD_JOKERS("game.jokers"), 
	 CMD_SELECTEDVARIANT("game.selectedVariant"),
	 CMD_UPDATEPLAYERCOUNT("lobby.playerCount"),
	 CMD_ROWCLICK("lobby.rowClick"),
	 CMD_ROUNDINFO("game.roundInfo"),
	 CMD_PRIVATE_PASSCODE("lobby.privatePasscode"),
	 CMD_ROOM_ADD("lobby.roomAdd"),
	 CMD_ROOM_DELETE("lobby.roomDelete");
	


private String text;	
	
	Commands(String text)
	{
		this.text = text;
	}
	@Override
	public String toString()
	{
		return this.text;
	}
	public static Commands fromString(String text) 
	{
	    if (text != null)
	    {
	      for (Commands b : Commands.values()) 
	       {
	    	  if (text.equalsIgnoreCase(b.text)) {
				return b;
			}
	      }
	    }
	    return CMD_BADCOMMAND;
	  }
	public static void main(String[] args) {
		System.out.println(Commands.CMD_CLIENTREADY);
		System.out.println(Commands.CMD_CLIENTREADY);
	}
}


