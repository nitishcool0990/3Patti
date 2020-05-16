package Gt.common;

public enum PlayerActionType {
	BLIND("blind"),
	CHAAL("chaal"),
	PACK("pack"),
	SHOW("show"),
	SIDE_SHOW("sideShow"),
	SIDE_SHOW_ACCEPT("Accepted"),
	SIDE_SHOW_DENIED("Denied"),
	SEEN("Seen"),
	ALLIN("AllIn");
private String text;

	PlayerActionType(String text)
	{
		this.text = text;
	}

	@Override
	public String toString() {
		return this.text;
	}

	public static PlayerActionType fromString(String text) {
		if (text != null) {
			for (PlayerActionType b : PlayerActionType.values()) {
				if (text.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		return PACK;
	}
}
