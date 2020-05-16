package Gt.room.Extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import Gt.interfaces.IGameExtension;

public class LeaveRoomHandler extends BaseServerEventHandler{
	
	public static Logger log = LoggerFactory.getLogger("Gt.room.Extension.GameExtension");
	@Override
	public void handleServerEvent(ISFSEvent arg0) throws SFSException {
		log.info("handling leave room server event");
		
		IGameExtension gameExt = (IGameExtension)getParentExtension();
		User user =   (User) arg0.getParameter(SFSEventParam.USER);
	    gameExt.handleLeaveRoom(user);
	}

}
