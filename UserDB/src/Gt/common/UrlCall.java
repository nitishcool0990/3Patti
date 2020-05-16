package Gt.common;

public enum UrlCall {

API_BADCOMMAND(""),
API_MY_ACCOUNT("user/finance/get_user_balance"),
API_WITHDREW("user/finance/withdraw"),
API_DEPOSIT("user/finance/server_deposit"),
API_TRANSACTION_HISTORY("user/finance/server_create_game_transaction"),
API_WAGER_RECORD("user/finance/create_user_wager"),
API_TRANSACTION_UPDATE("user/finance/server_update_game_transaction");
private String text;	
	
UrlCall(String text)
	{
		this.text = text;
	}
	@Override
	public String toString()
	{
		return this.text;
	}
	public static UrlCall fromString(String text) 
	{
	    if (text != null)
	    {
	      for (UrlCall b : UrlCall.values()) 
	       {
	    	  if (text.equalsIgnoreCase(b.text)) {
				return b;
			}
	      }
	    }
	    return API_BADCOMMAND;
	  }
	public static void main(String[] args) {
		System.out.println(UrlCall.API_MY_ACCOUNT);
		System.out.println(UrlCall.API_MY_ACCOUNT);
	}
}
