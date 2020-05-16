package Gt.main.Extension;

import com.smartfoxserver.v2.annotations.MultiHandler;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

import Gt.common.Commands;
import Gt.interfaces.IGameExtension;
import Gt.interfaces.IThreePattiExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MultiHandler
public class ThreePattiExtRequestHandler extends BaseClientRequestHandler{
	//private static Logger log = LoggerFactory.getLogger("extensions.games.main.ThreePattiExtension");
	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		String requestId =  params.getUtfString(SFSExtension.MULTIHANDLER_REQUEST_ID);
		IThreePattiExtension threePattiExt = (IThreePattiExtension) getParentExtension();
		 Commands cmd = Commands.fromString("lobby."+requestId);
		 
		switch(cmd){
		case CMD_KEEPALIVE:
			threePattiExt.handleKeepAlive(user);
			break;
		
		case CMD_GAMEACCOUNT:
			threePattiExt.sendMyAccount(user);
			
			break;
		
		case CMD_CLIENTEXCEPTION:
			String clientException = params.getUtfString("exception");
			threePattiExt.printClientException(user,clientException);
			break;
		
		case CMD_ROWCLICK:
			String roomName = params.getUtfString("roomName");
			threePattiExt.handleRowClick(roomName,user);
			break;
		case CMD_PRIVATE_PASSCODE:
			String passcode = params.getUtfString("passcode");
			String privateRoomName = params.getUtfString("roomName");
			threePattiExt.handlePrivateTableResponse(passcode, privateRoomName, user);
			break;
			
		/*case "game.seatChipInfo":
			//After allowed take seat client will send this command to open a pop-up of money
			break;
		case "game.takeSeat":
			//pLAYER TAKE SEAT OR NOT and than spec_to_player
			break;
		case "game.myAccount":
			//Player account will be updated
			break;
		case "game.playerAction":
			//in game player send game action
			break;
		case "game.sideShow":
			//player side show
			break;
		case "game.leaveSeat":
			//player leave seat than put player from plyer_to_spec
			break;*/
		default:
			System.out.println("Not a command");
			
		
		}
		
	}

	
}
