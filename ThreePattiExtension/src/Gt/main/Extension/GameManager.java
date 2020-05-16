package Gt.main.Extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartfoxserver.v2.entities.Room;

import Gt.Base.BaseGameManager;
import Gt.HibernateMapping.GtGameConfig;
import Gt.HibernateMapping.GtGameProp;
import Gt.HibernateMapping.GtGameTable;
import Gt.common.Table;
import Gt.interfaces.Controller;
import Gt.interfaces.IGameExtension;
import Gt.utils.GtGameConfigUtils;
import Gt.utils.GtGamePropUtils;
import Gt.utils.GtGameTableUtils;

public class GameManager extends BaseGameManager {
	public static Logger log = LoggerFactory.getLogger("Gt.main.Extension.ThreePattiExtension");
	private ThreePattiExtension tp = null;

	public GameManager(ThreePattiExtension threePattiExtension) {
		this.tp = threePattiExtension;
	}

	public void init() {
	}

	public boolean manageGame() {
		log.info("Running game Manager");
		List<GtGameConfig> games = GtGameConfigUtils.getAllConfig();
		GtGameTable table;
		Room room = null;
		IGameExtension gameExt = null;
		Controller cont = null;
		Table gameTable = null;
		List<Room> dynamicRooms = null;
		ArrayList<String> dynamicRoomsName = new ArrayList<String>();
		ArrayList<String> roomsToDel = new ArrayList<String>();
		GtGameProp gtPropMaxPotLimit = null;
		int maxPotLimit = -1;
		for (GtGameConfig game : games) {
			gtPropMaxPotLimit = GtGamePropUtils.findByProperty(game.getGameConfigId(), "max_pot_limit");
			if(gtPropMaxPotLimit != null) {
				maxPotLimit = Integer.parseInt(gtPropMaxPotLimit.getPropValue());
			}
			log.info(" game " + game.getGameVariant() + " game config id " + game.getGameConfigId());

			String gameStatus = game.getStatus();

			switch (gameStatus) {
			case GtGameConfigUtils.GC_NEW:
				createGameTable(game, game.getMinRoom(), GtGameTableUtils.GT_NEW);
				createGameRoom1(this.tp, game);
				changeGameConfigStatus(game, GtGameConfigUtils.GC_CREATED);
				break;

			case GtGameConfigUtils.GC_CREATED:
				List<GtGameTable> gameTables = GtGameTableUtils.getActiveGameTables(game.getGameConfigId());
				int numberOfRooms = 0;

				for (Iterator localIterator2 = gameTables.iterator(); localIterator2.hasNext();) {
					table = (GtGameTable) localIterator2.next();
					room = tp.getParentZone().getRoomByName(table.getTableName());
					if (room == null && table.getTableStatus().equals(GtGameTableUtils.GT_CREATED)) {
						numberOfRooms++;
						Room createdRoom = createGameRoom2(this.tp, game, table);
						/*
						 * numberOfEmptyRooms++; roomNameToRemove.add(table.getTableName()); }else{
						 * this.updateGameTable(table.getTableName());
						 */
					} else if (room != null) {
						numberOfRooms++;
						gameExt = (IGameExtension) room.getExtension();
						cont = gameExt.getController();
						gameTable = cont.getTable();
						if (gameTable.getAllSeatOccOrReserver() == gameTable.getMaxPlayers()) {
							numberOfRooms--;
						}
					}
				}
				if (numberOfRooms <= game.getMinRoom()) {
					dynamicRoomsName = new ArrayList<String>();
					createGameTable(game, game.getMinRoom() - numberOfRooms, GtGameTableUtils.GT_NEW);
					dynamicRooms = createGameRoom1(this.tp, game);
					for(Room room1: dynamicRooms) {
						dynamicRoomsName.add(room1.getName());
					}
					if(!dynamicRoomsName.isEmpty())
						this.tp.handleRoomAdd(game, dynamicRoomsName,maxPotLimit);	
				} else {
					
					for (GtGameTable gtGameTable : gameTables) {
						if (numberOfRooms > game.getMinRoom()) {
							room = tp.getParentZone().getRoomByName(gtGameTable.getTableName());
							if (room != null) {
								gameExt = (IGameExtension) room.getExtension();
								cont = gameExt.getController();
								gameTable = cont.getTable();
								if (gameTable.getPlayers().size() < 1) {
									numberOfRooms--;
									this.updateGameTable(gtGameTable.getTableName());
									roomsToDel.add(gtGameTable.getTableName());
									room.destroy();
								}
							}
						}
					}
				
				}
				break;

			case GtGameConfigUtils.GC_HIDE:
				changeGameConfigStatus(game, GtGameConfigUtils.GC_BAD);

			case GtGameConfigUtils.GC_BAD:
				List<GtGameTable> gameTableToRemove = GtGameTableUtils.getActiveGameTables(game.getGameConfigId());
				Room delRoom = null;
				for (GtGameTable roomToRemove : gameTableToRemove) {
					this.updateGameTable(roomToRemove.getTableName());
					delRoom = this.tp.getParentZone().getRoomByName(roomToRemove.getTableName());
					if (delRoom != null) {
						roomsToDel.add(roomToRemove.getTableName());
						delRoom.destroy();
					}
				}

				break;

			default:
				break;

			}
			// trace(new Object[] { "Need to check gtgameconfig status value" });
		}
		
		if(!roomsToDel.isEmpty() && roomsToDel != null) {
			this.tp.handleRoomDelete(roomsToDel);
		}
		return false;
	}

	protected void updateGameTable(String roomName) {
		GtGameTable gameTable = GtGameTableUtils.getGameTable(roomName);
		gameTable.setTableStatus(GtGameTableUtils.GT_INACTIVE);
		GtGameTableUtils.saveGameTable(gameTable);
		Room room = tp.getParentZone().getRoomByName(roomName);
		if (room != null) {
			tp.getApi().removeRoom(room, true, false);
		}
	}

}
