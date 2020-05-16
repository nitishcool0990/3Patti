package Gt.room.Extension;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import Gt.interfaces.IGameExtension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDisconnectHandler
  extends BaseServerEventHandler
{
  private Logger log = LoggerFactory.getLogger("Gt.main.Extension.UserDisconnectHandler");
  
  public void handleServerEvent(ISFSEvent arg0)
    throws SFSException
  {
    this.log.trace("UserDisconnectHandler,Handling Server Event(UserDisconnectHandler)");
    
    IGameExtension ext = (IGameExtension)getParentExtension();
    User user = (User)arg0.getParameter(SFSEventParam.USER);
    if (user != null)
    {
      this.log.info("UserDisconnectHandler,User {} disconnected .", user.getName());
      if (ext != null) {
        ext.handleUserDisconnection(user);
      }
    }
  }
}
