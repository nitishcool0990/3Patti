package Gt.common;

public enum APIActionType {
	ADMIN(0),
	JOIN_GAME(1),
	GAME_CANCEL(2),
	GAME_WON(3),
	LEAVE_SEAT(10),
	RETURN_ACCESS_AMOUNT(13),
	RAKE(14),
	TOP_UP(15),
	GAME_LOST(16);
	private int apiActionType;

	APIActionType(int apiActionType)
	{
		this.apiActionType = apiActionType;
	}

	/*public int getInteger() {
		return Integer.parseInt(this.apiActionType);
	}*/

	public static APIActionType fromString(int apiActionType) {
		//if (actionType != null) {
			for (APIActionType b : APIActionType.values()) {
				if (apiActionType == b.apiActionType) {
					return b;
				}
			}
		//}
		return GAME_CANCEL; // if wrong action type taken then game cancel
	}
	public static void main(String[] args) {
		System.out.println(APIActionType.GAME_WON.getApiActionType());
	}

	public int getApiActionType() {
		return apiActionType;
	}

	
}
