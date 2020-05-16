package Gt.main.Extension;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;

import Gt.HibernateMapping.GtDomainName;
import Gt.HibernateMapping.GtGameConfig;
import Gt.HibernateMapping.GtGameTable;
import Gt.common.Commands;
import Gt.common.SeatInfo;
import Gt.common.UserJoinedRoomsUtil;
import Gt.interfaces.IGameExtension;
import Gt.interfaces.IThreePattiExtension;
import Gt.user.utils.GtGameAccountsUtils;
import Gt.user.utils.GtUserAccountUtils;
import Gt.user.utils.GtUserUtils;
import Gt.utils.GtDomainNameUtils;
import Gt.utils.GtGameTableUtils;


public class ThreePattiExtension
  extends SFSExtension
  implements IThreePattiExtension
{
	 //SmartFoxServer sfs = SmartFoxServer.getInstance();
	 public static Logger log = LoggerFactory.getLogger("Gt.main.Extension.ThreePattiExtension");
	 private String domainUrl = "";
	 UserJoinedRoomsUtil joinedRoomsUtil = new UserJoinedRoomsUtil();
  public void init()
  {
	 log.info("ThreePatti Extension initated time ="+new Date());
	 SmartFoxServer.getInstance().getEventManager().setThreadPoolSize(2);
	 
	 GtDomainName domain = GtDomainNameUtils.getTeenPattiDomain();
	 log.info("domain " + domain.toString());
	 
	 this.domainUrl = domain.getDomainUrl();
	 
	 
    addEventHandler(SFSEventType.USER_LOGIN, UserLoginHandler.class);
    addEventHandler(SFSEventType.USER_JOIN_ZONE, UserJoinZoneHandler.class);
    addEventHandler(SFSEventType.USER_DISCONNECT, UserLogoutHandler.class);
    addEventHandler(SFSEventType.USER_RECONNECTION_TRY, UserLogoutHandler.class);
    addEventHandler(SFSEventType.USER_LOGOUT, UserLogoutHandler.class);
    addRequestHandler("lobby", ThreePattiExtRequestHandler.class);
    // User Join Zone
    if (!createLobby()) {
        log.info("TeenPattiExtension, Lobby Already exists. Cant create again.");
      }
    
    
    SmartFoxServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new GameManagerTask(this), 20, 60, TimeUnit.SECONDS);
    
    SmartFoxServer.getInstance().getTaskScheduler().scheduleAtFixedRate(new UpdatedLobbyUserCountTask(), 90, 5, TimeUnit.SECONDS);
  }
  
  private class GameManagerTask
    implements Runnable
  {
    GameManager manager = null;
    
    public GameManagerTask(ThreePattiExtension threePattiExtension)
    {
    	log.info("Start Game manager Task");
      manager = new GameManager(threePattiExtension);
    }
    
    public void run()
    {
      try
      {
        if (manager.manageGame()) {
         //time to stop the task
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
        log.error("GameManagerTask",e);
      }
    }
  }
  
  public class UpdatedLobbyUserCountTask
  implements Runnable
{
  public UpdatedLobbyUserCountTask() {}
  
  public void run()
  {
    try
    {
      ThreePattiExtension.this.sendUpdatedUserCountForConfigAndTables();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      log.error("UpdatedLobbyUserCountTask",e);
    }
  }
}

  
  private boolean createLobby()
  {
      log.info("TeenPattiExtension,Creating Lobby.");
    if (getParentZone().getRoomByName("Lobby") != null)
    {
      log.info("TeenPattiExtension,Lobby already exist, Can't create it again.");
      return false;
    }
    CreateRoomSettings lobbySettings = new CreateRoomSettings();
    
    lobbySettings.setName("Lobby");
    
    lobbySettings.setDynamic(false);
    lobbySettings.setMaxUsers(10000);
    try
    {
      getApi().createRoom(getParentZone(), lobbySettings, null);
    }
    catch (Exception e)
    {
      log.info("TeenPattiExtension,Room was not created.Returning from createLobby().");
      log.error("createLobby", e);
    }
    return true;
  }


@Override
	public void sendAllGameRoom(User user) {
		// TODO Auto-generated method stub
		log.info("Sending game rooms to user " + user.getName());
		SFSObject sfs = new SFSObject();
		int userId = ((Integer) user.getSession().getProperty("PlayerId")).intValue();
		List<String> roomlist = new ArrayList<String>(this.joinedRoomsUtil.getJoinedRooms(userId));

		log.info("onZoneJoinEvent the joinedRoomsUtils returned rooms: {}", roomlist);

		List<String> newRoomList = new ArrayList<String>();
		for (String roomName : roomlist) {
			Room room = getParentZone().getRoomByName(roomName);
			if (room == null) {
				this.joinedRoomsUtil.leaveRoom(userId, roomName);
			}else{
				newRoomList.add(roomName);
			}
		}
		
		
		log.info("onZoneJoinEvent the sent rooms: {}", newRoomList);

		sfs.putUtfString("roomList", newRoomList.toString());

		sendCommand(Commands.CMD_USERROOMLIST, sfs, user);
	}


private User getUserByPlayerId(int playerId){
	User user = null;
	user =	getApi().getUserById(playerId);
	return user;
	
}

public void sendCommand(Commands cmd,SFSObject obj, User user){
		send(cmd.toString(), obj,user);
}

public void sendCommand(Commands cmd,SFSObject obj, int playerID){
	if(getApi().getUserById(playerID)!=null){
		send(cmd.toString(), obj, getUserByPlayerId(playerID));
	}else{
		System.out.println("No user found at game extension");
	}
}


public void destroy()
{
  removeEventHandler(SFSEventType.USER_LOGIN);
  removeEventHandler(SFSEventType.USER_JOIN_ZONE);
  removeEventHandler(SFSEventType.USER_DISCONNECT);
  removeEventHandler(SFSEventType.USER_RECONNECTION_TRY);
  removeEventHandler(SFSEventType.USER_LOGOUT);
  removeRequestHandler("lobby");
  super.destroy();
}

public UserJoinedRoomsUtil getJoinedRoomsUtil()
{
  return this.joinedRoomsUtil;
}


public void onUserRemove(User user)
{
  int userId = ((Integer)user.getSession().getProperty("PlayerId")).intValue();
  
  this.joinedRoomsUtil.updatLastTime(userId);
}

public String getPlayerSession(User user) {
	String ret = null;
	if (user == null) {
		log.debug(" Value of user is NULL . Returning from getPlayerSession().");
		return null;
	}
	ret = (user.getSession().getProperty("sessionToken")).toString();
	log.info("getPlayerSession: PlayerId of User " + user.getName() + " is " + ret);
	return ret;
}

@Override
public void sendMyAccount(User user) {
	// TODO Auto-generated method stub
	SFSObject sfso = new SFSObject();
	
	log.info("game.myAccount received");
	int playerId = ((Integer) user.getSession().getProperty("PlayerId")).intValue();
/*	String authToken = getPlayerSession(user);
	if(authToken == null){
		log.error("Authtoken is null for playerid "+playerId);
		return ;
	}
	HashMap<String, String> params = new HashMap<>();
	params.put("Authorization", authToken );
	
	JSONObject info = GtUserAccountsUtils.getPlayerInfo("192.168.0.204:9000", "/api/info/", authToken);
	
	log.info("$$$$$$$$$ GET PLAYERINFO FROM API CALL " + info);*/
	
	//GtUserAccount myAccount = GtUserAccountUtils.findByPlayerId(playerId);
	String ret = user.getSession().getProperty("sessionToken").toString();
	
	Map<String, Object> account = GtUserUtils.checkPlayer(this.domainUrl, ret, playerId);
	Double realBalance = 0.0;
	Double dummyBalance = 0.0;
		realBalance = (Double) account.get(GtUserAccountUtils.REAL);
		dummyBalance = (Double) account.get(GtUserAccountUtils.DUMMY);
		
	if(account != null){
		sfso.putDouble("realBalance", realBalance);
		sfso.putDouble("dummyBalance", dummyBalance);
	}
	
	sendCommand(Commands.CMD_GAMEACCOUNT, sfso, user);
	

}


@Override
public void printClientException(User user,String clientException) {
	
	SFSObject sfso = new SFSObject();
	int playerId = getPlayerId(user);
	
	log.info("Client Side Exception occured for player id " + playerId);
	log.info("Stack trace  " + clientException);
	
}


@Override
public void handleKeepAlive(User user) {
	
	SFSObject sfs = new SFSObject();
	log.info("PlayerId sending Keep Alive"+getPlayerId(user));
	send(Commands.CMD_KEEPALIVE.toString(), sfs, user);
	
}

protected int getPlayerId(User user) {
	int ret = -1;
	if (user == null) {
		log.debug(" Value of user is NULL . Returning from getPlayerId().");
		return -1;
	}
	if (user.getVariable("PlayerId") != null) {
		ret = user.getVariable("PlayerId").getIntValue().intValue();
	} else {
		ret = ((Integer) user.getSession().getProperty("PlayerId")).intValue();
	}
	log.info("getPlayerId: PlayerId of User " + user.getName() + " is " + ret);
	return ret;
}

public void sendUpdatedUserCountForConfigAndTables(){
	try{
		log.info("Inside update count method");
		List<GtGameTable> gameTablelist = GtGameTableUtils.getAllActiveGameTables();
		List<String> tableName = new ArrayList<String>();
		for(GtGameTable gameTable :gameTablelist){
			tableName.add(gameTable.getTableName());
		}
		ArrayList<HashMap<String, String>> dataMap = GtGameAccountsUtils.getPlayerCount(tableName);
		log.info("Player count Datamap is "+dataMap);
		StringBuilder roomWithCount = new StringBuilder();
		for (HashMap<String, String> data : dataMap){
				roomWithCount.append(data.get("roomName")+"@"+String.valueOf(data.get("playerCount"))+",");
				
		}
		 @SuppressWarnings("unchecked")
		List<User> userList = new ArrayList<>(getParentZone().getUserList());
		 
		 if(roomWithCount.length()>0){
			 roomWithCount.deleteCharAt(roomWithCount.length()-1);
		 }
		 log.info("sending room with Count " + roomWithCount);
		log.info("sending lobby size " + userList.size());
		SFSObject sfsObj = new SFSObject();
		sfsObj.putUtfString("data", roomWithCount.toString());
		sfsObj.putInt("lobby", userList.size());
		send(Commands.CMD_UPDATEPLAYERCOUNT.toString(), sfsObj, userList);
	}catch(Exception e){
		e.toString();
		log.error("Error in update count method ",e);
	}
}


@Override
public void handleRowClick(String roomName,User user) {
	// TODO Auto-generated method stub
	Room room = getParentZone().getRoomByName(roomName);
//	log.info("row click request for room " + room.getName() + " of zone " + getParentZone().getName());
	//SeatInfo seatInfo = (SeatInfo) room.getExtension().handleInternalMessage(Commands.CMD_ROWCLICK.toString(),null);
	IGameExtension gameExt =  (IGameExtension) room.getExtension();
	SeatInfo seatInfo = gameExt.getController().getSeatInfo();
	log.info("inside handle row click " + seatInfo.toString());
	send(Commands.CMD_ROWCLICK.toString(), seatInfo.toSFSObject(), user);
}

@Override
public String getDomainUrl() {
	return domainUrl;
}


@Override
public void handlePrivateTableResponse(String passcode,String roomName, User user) {
	try{
		log.info("Handle Private Table");
		SFSObject sfsObj = new SFSObject();
		boolean isMatch =false;
		boolean flag = false;
		if (user == null) {
			flag = true;
			log.debug(" Value of user is NULL . Returning from getPlayerId().");
			sfsObj.putUtfString("msg", "User not exist on Game Zone!");
		}
		if(passcode == null){
			flag = true;
			log.debug(" Value of passcode is NULL . Coming from client.");
			sfsObj.putUtfString("msg", "Please enter password.");
		}
		if(roomName == null){
			flag = true;
			log.debug(" Value of roomName is NULL . Coming from client.");
			sfsObj.putUtfString("msg", "This room is not exists.");
		}
		if(!flag){
			log.info("User Input Parameters is passcode "+passcode+" roomName "+roomName);
			Room room = getParentZone().getRoomByName(roomName);
			if(room == null){
				log.debug(" Value of room is NULL ");
				return;
			}
			IGameExtension gameExt =  (IGameExtension) room.getExtension();
			//Get passcode from one of the getter from Game extension
			//check with passcode
			String tablePasscode= gameExt.getController().getPasscode();
			
			if(tablePasscode.equalsIgnoreCase(passcode)){
				isMatch = true;
			}else{
				sfsObj.putUtfString("msg", "Please enter correct password!");
			}
		}
		sfsObj.putBool("isMatch", isMatch);
		log.info("Response for private table is "+isMatch+" flag "+flag);
		sendCommand(Commands.CMD_PRIVATE_PASSCODE,sfsObj,user);
	}catch(Exception e){
		e.toString();
		log.error("Error while handle private table passcode ",e);
	}
}

public void handleRoomAdd(GtGameConfig gtConfig,ArrayList<String> roomName,int maxPotLimit) {
	SFSObject obj = new SFSObject();
	 @SuppressWarnings("unchecked")
		List<User> userList = new ArrayList<>(getParentZone().getUserList());
		 
	 obj.putInt("maxPotLimit", maxPotLimit);
	obj.putClass("config", gtConfig);
	obj.putUtfString("rooms",roomName.toString());
	send(Commands.CMD_ROOM_ADD.toString(), obj, userList);
}

public void handleRoomDelete(ArrayList<String> roomName) {
	SFSObject obj = new SFSObject();
	List<User> userList = new ArrayList<>(getParentZone().getUserList());
	 obj.putUtfString("test", "Nitish");
	obj.putUtfString("roomToDel",roomName.toString());
	send(Commands.CMD_ROOM_DELETE.toString(), obj, userList);
}




}
