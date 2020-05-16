package Gt.room.Extension;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import Gt.interfaces.IGameExtension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserReconnectionSuccessHandler
  extends BaseServerEventHandler
{
  private Logger log = LoggerFactory.getLogger(UserReconnectionSuccessHandler.class);
  
  public void handleServerEvent(ISFSEvent event)
    throws SFSException
  {
    this.log.info("UserReconnectionSuccessHandler,Handling Server Event(UserReconnectionSuccessHandler)");
    if (event == null)
    {
      this.log.debug("UserReconnectionSuccessHandler,Value of ISFSEvent is NULL. Returning from handleServerEvent().");
      return;
    }
    IGameExtension ext = (IGameExtension)getParentExtension();
    User user = (User)event.getParameter(SFSEventParam.USER);
    if (user != null)
    {
      this.log.info("UserReconnectionSuccessHandler,User {} reconnect success.", user.getName());
      if (ext != null) {
        ext.handleUserReconnectionSuccess(user);
      }
    }
  }
}
