package Gt.interfaces;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.SFSObject;

import Gt.common.Commands;
import Gt.common.UserJoinedRoomsUtil;

public abstract interface IThreePattiExtension {
	public abstract void sendAllGameRoom(User user);
	public abstract void sendCommand(Commands cmd,SFSObject obj, int playerID);
	public UserJoinedRoomsUtil getJoinedRoomsUtil();
	public  void sendMyAccount(User user);
	public  void printClientException(User user,String clientException);
	public void handleKeepAlive(User user);
	public abstract void handleRowClick(String roomName,User user);
	public String getDomainUrl();
	public void handlePrivateTableResponse(String passcode,String roomName,User user);
}