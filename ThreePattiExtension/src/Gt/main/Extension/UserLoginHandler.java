package Gt.main.Extension;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Gt.interfaces.IThreePattiExtension;
import Gt.user.utils.GtUserUtils;

import com.smartfoxserver.bitswarm.sessions.Session;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class UserLoginHandler extends BaseServerEventHandler {
	public static Logger log = LoggerFactory.getLogger("Gt.main.Extension.UserLoginHandler");
	@Override
	public void handleServerEvent(ISFSEvent arg0) throws SFSException {
		// TODO Auto-generated method stub
		
		log.info("Inside User Login Handler");
		String userName = (String) arg0.getParameter(SFSEventParam.LOGIN_NAME);
		String password = (String) arg0.getParameter(SFSEventParam.LOGIN_PASSWORD);
		ISFSObject sfsoIn = (ISFSObject) arg0.getParameter(SFSEventParam.LOGIN_IN_DATA);
		User user = getApi().getUserByName(userName); 
	       
	   /*   if (user != null) 
	      { 
	    	  SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_ALREADY_LOGGED);
		       errData.addParameter(userName);
		         
		        throw new SFSLoginException("User name " + userName + " already logged in.", errData); 
	      }*/
		log.info("Player with username="+userName+" doing login");

		int playerId = -1;
		String encSessionId  = "";
		String emailId ="";
		String playerName = "";
		if (sfsoIn != null) {
			encSessionId = sfsoIn.getUtfString("sessionToken");
			playerId = sfsoIn.getInt("playerId");
			emailId = sfsoIn.getUtfString("emailId");
			playerName = sfsoIn.getUtfString("name");
		}
		
		IThreePattiExtension ext = (IThreePattiExtension) getParentExtension();
		if(ext ==null){
			return;
		}
		
		//GtUser gtUser = GtUserUtils.getPlayerIdByName(userName);
		Map<String, Object> myAccountRes = GtUserUtils.checkPlayer(ext.getDomainUrl(), encSessionId, playerId);
		Session session = (Session) arg0.getParameter(SFSEventParam.SESSION);
		ISFSObject sfsoOut = (ISFSObject) arg0.getParameter(SFSEventParam.LOGIN_OUT_DATA);
	
		
		if(myAccountRes ==null){
	        SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
	        errData.addParameter(userName);
	         
	        throw new SFSLoginException("Invalid username", errData); 
		}
		log.info("Player with username="+userName+" having playerId = "+playerId+" in domain name = 192.168.1.51");
		session.setProperty("PlayerId", playerId);
		session.setProperty("sessionToken", encSessionId);
		session.setProperty("playerName", playerName);
		if(emailId != null) {
			session.setProperty("emailId", emailId);
		}

        String theClient = session.getSystemProperty("ClientType").toString();
        log.info("Client Type for player = "+playerId+" is = "+theClient);
		sfsoOut.putLong("playerId",playerId);

	}

}
