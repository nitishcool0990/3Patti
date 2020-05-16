package Gt.common;

public enum GameVariants {
	SIMPLE(1,"simple"),
	MUFLIS(2,"muflis"),
	AK47(3,"ak47"),
	JOKER(4,"joker"),
	FOURXBOOT(5,"4Xboot"),
	TWOXBOOT(6,"2Xboot"),
	ALL(7,"all");
	
	
	private String text;
	private int val;

	GameVariants(int val, String text)
	{
		this.val = val;
		this.text = text;
	}
	
	public int getVal() {
		return val;
	}

	@Override
	public String toString() {
		return this.text;
	}
	
	public static int getGameVariantVal(String variant) {
		int gameVar = -1;
		for(GameVariants gv: GameVariants.values()) {
			if(gv.toString().equalsIgnoreCase(variant))
				gameVar = gv.getVal();
		}
		return gameVar;
	}
}
