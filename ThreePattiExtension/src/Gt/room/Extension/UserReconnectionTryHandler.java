package Gt.room.Extension;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import Gt.interfaces.IGameExtension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserReconnectionTryHandler
  extends BaseServerEventHandler
{
  private Logger log = LoggerFactory.getLogger(UserReconnectionTryHandler.class);
  
  public void handleServerEvent(ISFSEvent event)
    throws SFSException
  {
    this.log.info("UserReconnectionTryHandler,Handling Server Event.");
    if (event == null)
    {
      this.log.debug("UserReconnectionTryHandler,Value of ISFSEvent is NULL. Returning from handleServerEvent().");
      return;
    }
    IGameExtension ext = (IGameExtension)getParentExtension();
    User user = (User)event.getParameter(SFSEventParam.USER);
    if (user != null)
    {
      this.log.info("UserReconnectionTryHandler,User {} trying to reconnect ", user.getName());
      if (ext != null) {
        ext.handleUserReconnectionTry(user);
      }
    }
  }
}
