package Gt.room.Extension;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartfoxserver.v2.annotations.MultiHandler;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSRoomException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

import Gt.common.Commands;

@MultiHandler
public class GameRequestHandler extends BaseClientRequestHandler {

	public static Logger log = LoggerFactory.getLogger("Gt.room.Extension.GameRequestHandler");

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		// TODO Auto-generated method stub
		String requestId = params.getUtfString(SFSExtension.MULTIHANDLER_REQUEST_ID);
		GameExtension gameExt = (GameExtension) getParentExtension();
		Commands cmd = Commands.fromString("game." + requestId);
		log.info("Game request handler cmd received " + cmd + " . params " + params.toString());
		try {
			switch (cmd) {
			case CMD_CLIENTREADY:
				gameExt.handleClientReady(user);
				break;
			case CMD_LEAVESEAT:
				int playerId = user.getPlayerId();
				gameExt.handleLeaveSeat(user);

				break;

			case CMD_PLAYERTOOKACTION:
				String playerAction = params.getUtfString("playerAction");
				int seatId = 0;
				int chipAmount = params.getInt("chipAmount");
				boolean isUserTurn = false;
				if(params.containsKey("isUserTurn")){
					isUserTurn = params.getBool("isUserTurn");
				}

				gameExt.handlePlayerTookAction(user, playerAction, seatId, chipAmount,isUserTurn);

				// gameExt.getController().playerTookAction(playerAction,user.getPlayerId(),seatId,chipAmount);

				break;

			case CMD_SIDESHOWRESPONSE:
				int res = params.getInt("res");
				gameExt.handleSideShowRes(user, res);
				break;
			
			case CMD_DEALERRESPONSE:
				String gameVariant = params.getUtfString("gameVariant");
				gameExt.handleDealerResponse(gameVariant);
				break;


			case CMD_SEE:
				int seeIndex = -1;
				if(params.containsKey("cIndex")){
					seeIndex = params.getInt("cIndex");
				}
				gameExt.handleSeeCards(user,seeIndex);
				break;

			case CMD_RESERVEDSEAT:
				// while seat is reserved
				int sId = params.getInt("seatId").intValue();
				boolean forReservation = params.getBool("reserve").booleanValue();
				boolean isWaiting = params.getBool("isWaiting").booleanValue();
				gameExt.handleSeatReservation(user, sId, forReservation, isWaiting);
				break;

			case CMD_BUYINDATA:
				boolean isTopUpRequest = false;
				if(params.containsKey("isTopUpRequest")){
					isTopUpRequest = params.getBool("isTopUpRequest");
				}
				gameExt.handleBuyInChipInfo(user,isTopUpRequest);
				break;
			case CMD_TAKESEAT:
				int seatid = 0;
				if(params.containsKey("seatId")){
					seatid = params.getInt("seatId").intValue();
				}
				int amt = params.getInt("amt").intValue();
				boolean isTopUp = false;
				if(params.containsKey("isTopUpRequest")){
					isTopUp = params.getBool("isTopUpRequest");
				}
				if (!gameExt.handleTakeSeat(user,seatid,amt,isTopUp)) {
					log.debug("GameRequestHandler,STT is running cant take Seat.");
				}

				// pLAYER TAKE SEAT OR NOT and than spec_to_player
				break;
			case CMD_ROUNDINFO:
				gameExt.handleRoundInfo(user);
				break;

			/*
			 * case "game.myAccount": //Player account will be updated break;
			 * case "game.playerAction": //in game player send game action
			 * break; case "game.sideShow": //player side show break; case
			 * "game.leaveSeat": //player leave seat than put player from
			 * plyer_to_spec break;
			 */
			default:
				System.out.println("Not a command dude");

			}
		} catch (Exception e) {
			log.info("Game request handler, error for cmd " + cmd + " . " + params.toString());
			log.info("Exception: "+e);
			log.error("Error", e);
			
		}
	}

}
