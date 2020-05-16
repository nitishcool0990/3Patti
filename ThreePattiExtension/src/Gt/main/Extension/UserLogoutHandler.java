package Gt.main.Extension;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLogoutHandler
  extends BaseServerEventHandler
{
  private static Logger log = LoggerFactory.getLogger("Gt.main.Extension.ThreePattiExtension");
  
  public void handleServerEvent(ISFSEvent event)
    throws SFSException
  {
    User user = (User)event.getParameter(SFSEventParam.USER);
    
    log.info("UserLogoutHandler_Handling Server UserLogoutHandler : {}", user.getName());
    
    ThreePattiExtension ext = (ThreePattiExtension)getParentExtension();
    ext.onUserRemove(user);
  }
}
