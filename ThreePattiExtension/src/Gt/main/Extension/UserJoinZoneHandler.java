package Gt.main.Extension;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import Gt.interfaces.IThreePattiExtension;

public class UserJoinZoneHandler extends BaseServerEventHandler {
	 public static Logger log = LoggerFactory.getLogger("Gt.main.Extension.ThreePattiExtension");
	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		// TODO Auto-generated method stub
		if(event==null){
			return;
		}
		User user = (User)event.getParameter(SFSEventParam.USER);
		if(user !=null){
			IThreePattiExtension ext = (IThreePattiExtension) getParentExtension();
			if(ext ==null){
				return;
			}
			 UserVariable uv_dbId = new SFSUserVariable("PlayerId", user.getSession().getProperty("PlayerId"));
			 log.info("new SFS User variable PLayerId created with value " + user.getSession().getProperty("PlayerId"));
		      
		     UserVariable uv_appName = new SFSUserVariable("appName", user.getSession().getProperty("appName"));
		     log.info("new SFS USer variable appName created with value " + user.getSession().getProperty("appName"));
		      

		      List<UserVariable> vars = Arrays.asList(new UserVariable[] { uv_dbId, uv_appName });
		      getApi().setUserVariables(user, vars);
		      log.info("User Variables playerId = " + user.getSession().getProperty("PlayerId") + " and appName = " + user.getSession().getProperty("appName") + " set to user " + user.getName());
		      
			ext.sendAllGameRoom(user);
			log.info("Game Rooms sent to user " + user.getName());
		}
		
		
		
	}

}
