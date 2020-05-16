package Gt.room.Extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import Gt.interfaces.IGameExtension;

public class JoinRoomHandler
  extends BaseServerEventHandler
{	
	
  public static Logger log = LoggerFactory.getLogger("Gt.room.Extension.GameExtension");
  
  public void handleServerEvent(ISFSEvent arg0)
    throws SFSException
  {
    IGameExtension gameExt = (IGameExtension)getParentExtension();
    if(gameExt==null){
    	log.debug("game Extension found null");
    	return;
    }
    User user =   (User) arg0.getParameter(SFSEventParam.USER);
    log.info("Handling join room request for User  " + user.getName());
    gameExt.handleJoinRequest(user);
  }
}
