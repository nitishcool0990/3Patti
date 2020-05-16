package Gt.Base;

import java.util.ArrayList;
import java.util.List;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.api.ISFSGameApi;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.extensions.ISFSExtension;
import com.smartfoxserver.v2.game.CreateSFSGameSettings;

import Gt.HibernateMapping.GtGameConfig;
import Gt.HibernateMapping.GtGameTable;
import Gt.utils.GtGameConfigUtils;
import Gt.utils.GtGameTableUtils;

public class BaseGameManager {
	  private static final String TablePrefix = "#";
	  private static final String gameExtension = "Gt.room.Extension.GameExtension";
	 public void changeGameConfigStatus(GtGameConfig game,String updatedStatus) {
		  game.setStatus(updatedStatus);
	      GtGameConfigUtils.saveGameConfigTable(game);
		
	}

	protected static List<Room> createGameRoom1(ISFSExtension zoneExtension, GtGameConfig gameConfig)
	  {
		List<Room> rooms = new ArrayList<Room>();
	    List<GtGameTable> gtGametables = GtGameTableUtils.getActiveGameTables(gameConfig.getGameConfigId());
	    for (GtGameTable gt: gtGametables)
	    {
	     // GtGameTable gt = gtGametables.get(0);
	      if ((gt.getTableName() == null) && (gt.getTableStatus().equals(GtGameTableUtils.GT_NEW)))
	      {
	        String tableName = gameConfig.getGameVariant() +TablePrefix+gameConfig.getMaxPlayers() + 
	        		TablePrefix+ "-TP" + gt.getId();
	        CreateSFSGameSettings gameSettings = createDefaultSettings(tableName, gameConfig.getGameVariant(),gameConfig.getMaxPlayers(),gameConfig.getMinPlayers());
	        gameSettings.setExtension(new CreateRoomSettings.RoomExtensionSettings(zoneExtension.getName(), gameExtension));
	        

	        String gcType = gameConfig.getGameVariant();
	        

	        List<RoomVariable> rv = new ArrayList<RoomVariable>();
	        
	        rv.add(new SFSRoomVariable("gametable.id", gt.getId()));
	        rv.add(new SFSRoomVariable("gameType", gameConfig.getGameVariant()));
	        rv.add(new SFSRoomVariable("status", gt.getTableStatus()));
	        rv.add(new SFSRoomVariable("gcType", gcType));
	        rv.add(new SFSRoomVariable("gameName", tableName));
	        

	        RoomVariable rvConfigId = new SFSRoomVariable("configId", gameConfig.getGameConfigId());
	        rvConfigId.setGlobal(true);
	        rv.add(rvConfigId);
	        

	        gameSettings.setRoomVariables(rv);
	        
	        Room room = createSFSGameRoom(zoneExtension.getParentZone(), gameSettings, tableName);
	        if (room != null)
	        {
	          gt.setTableName(room.getName());
	          gt.setTableStatus(GtGameTableUtils.GT_CREATED);
	          GtGameTableUtils.saveGameTable(gt);
	          rooms.add(room);
	        }
	      }
	    }
	    return rooms;
	  }
	
	
	protected static Room createGameRoom2(ISFSExtension zoneExtension, GtGameConfig gameConfig,GtGameTable gtGameTable)
	  {
		Room room = null;
	    /*for (GtGameTable gt: gtGametables)
	    {*/
	     // GtGameTable gt = gtGametables.get(0);
	      if (gtGameTable.getTableName() != null && gtGameTable.getTableStatus().equals(GtGameTableUtils.GT_CREATED))
	      {
	        String tableName = gtGameTable.getTableName();
	        CreateSFSGameSettings gameSettings = createDefaultSettings(tableName, gameConfig.getGameVariant(),gameConfig.getMaxPlayers(),gameConfig.getMinPlayers());
	        gameSettings.setExtension(new CreateRoomSettings.RoomExtensionSettings(zoneExtension.getName(), gameExtension));
	        

	        String gcType = gameConfig.getGameVariant();
	        

	        List<RoomVariable> rv = new ArrayList<RoomVariable>();
	        
	        rv.add(new SFSRoomVariable("gametable.id", gtGameTable.getId()));
	        rv.add(new SFSRoomVariable("gameType", gameConfig.getGameVariant()));
	        rv.add(new SFSRoomVariable("status", gtGameTable.getTableStatus()));
	        rv.add(new SFSRoomVariable("gcType", gcType));
	        rv.add(new SFSRoomVariable("gameName", tableName));
	        

	        RoomVariable rvConfigId = new SFSRoomVariable("configId", gameConfig.getGameConfigId());
	        rvConfigId.setGlobal(true);
	        rv.add(rvConfigId);
	        

	        gameSettings.setRoomVariables(rv);
	        
	         room = createSFSGameRoom(zoneExtension.getParentZone(), gameSettings, tableName);
	      }
	  //  }
	    return room;
	  }
	  
	  protected static CreateSFSGameSettings createDefaultSettings(String tableName, String gameVarient, Integer maxP, Integer minP)
	  {
	    CreateSFSGameSettings gameSettings = new CreateSFSGameSettings();
	    gameSettings.setName(tableName);
	    gameSettings.setGroupId("default");
	    gameSettings.setMinPlayersToStartGame(minP);
	    gameSettings.setMaxUsers(maxP);
	    gameSettings.setMaxSpectators(5000);
	    gameSettings.setGamePublic(true);
	    gameSettings.setLeaveLastJoinedRoom(false);
	    gameSettings.setMaxVariablesAllowed(10);
	    gameSettings.setGame(true);
	    
	    return gameSettings;
	  }
	  
	  
	  
	  private static Room createSFSGameRoom(Zone zoneName, CreateSFSGameSettings gameSettings, String tableName)
	  {
	    ISFSGameApi gameApi = SmartFoxServer.getInstance().getAPIManager().getGameApi();
	    Room room = null;
	    try
	    {
	      room = gameApi.createGame(zoneName, gameSettings, null, true, false);
	    }
	    catch (SFSCreateRoomException e)
	    {
	      System.out.println("$$$$$$$="+tableName);
	      e.printStackTrace();
	    }
	    return room;
	  }
	  
	  protected void createGameTable(GtGameConfig gt, int noOfRoomsToCreate,String status)
	  {
	    for (int noOfRooms = 0; noOfRooms < noOfRoomsToCreate; noOfRooms++)
	    {
	      GtGameTable gtGameTable = new GtGameTable();
	      gtGameTable.setGameConfigId(gt.getGameConfigId());
	      gtGameTable.setTableStatus(status);
	      GtGameTableUtils.saveGameTable(gtGameTable);
	    }
	  }
	  
	 
	  private void updateRoomVariables(Room room){
		  if(room.getVariable("status").equals(GtGameTableUtils.GT_NEW)){
			 // room.set
		  }
	  }
}
