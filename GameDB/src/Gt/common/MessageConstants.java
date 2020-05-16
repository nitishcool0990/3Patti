package Gt.common;

public enum MessageConstants {
	MSG_WAIT(801,"Please wait for other players to join"),
	MSG_GAME_VARIANT_INPROGRESS(802,"Game Variant selection in progress"),
	MSG_GAME_VARIANT_SELECTED(803,"Game Variant: "),
	MSG_CONTACT_ADMIN(804,"Contact Admin, Something went wrong!"),
	MSG_TOPUP_SUCCESS(805," chips added to table"),
	MSG_INSUFFICIENT_BALANCE(806," Insufficient funds in wallet"),
	MSG_SIDESHOW_REQUEST(807," requested sideshow to "),
	MSG_SIDESHOW_CANCELLED(808,"Sideshow cancelled"),
	MSG_SIDESHOW_DENIED(809,"Sideshow denied"),
	MSG_SESSION_EXPIRED(810,"session expired."),
	MSG_BETTING_OVER(811,"Betting round over. Please wait for next round to begin"),
	MSG_TAKE_SEAT(812,"Please take seat to continue."),
	MSG_MAX_MONEY(813,"You have more money than the allowed limit on this table."),
	MSG_TOPUP_REQUEST_SUCCESS(814," chips will be added after current round ends"),
	MSG_NO_ROUND_HISTORY(815,"No round history exists."),
	MSG_SHOWDOWN(816,"Showdown is in progress.");
	
	private final String msg;
	private final int msgCode;
	
	private MessageConstants(int msgCode, String msg){
		this.msg = msg;
		this.msgCode = msgCode;
	}
	
	public String getMessage(){
		return this.msg;
	}
	
	public int getMsgCode(){
		return this.msgCode;
	}
	
}
