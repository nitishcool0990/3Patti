package Gt.user.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hibernate.Session;
import org.hibernate.Transaction;

import Gt.common.UrlCall;
import Gt.test.WebSericeCalling;
import Gt.user.hibernateMapping.GtUser;
import Gt.user.hibernateMapping.GtUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
public class GtUserUtils {

	private static GtUserDAO dao = new GtUserDAO();
	public static Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");
	
	
	public static GtUser getPlayerIdByName(String playerName){
		GtUser gtUser =null;
		Session session =null;
		Transaction tx = null;
		try{
			log.info("getting player id of user " + playerName);
			session = dao.getSession();
			tx = session.beginTransaction();
			List<GtUser> gtUsers = dao.getByPlayerName(playerName);
			if(gtUsers!=null && !gtUsers.isEmpty())
				gtUser =gtUsers.get(0);
				log.info("returning GtUSer instance " + gtUser.toString());
				tx.commit();
		}catch(Exception e){
			log.error("failed to get player id ", e);
			e.printStackTrace();
			if(tx != null){
				tx.rollback();
			}
			
			
		}finally {
			if(session != null && session.isOpen()){
				session.close();
			}
		}
	
		
	return gtUser;	
	}
	
	public static Map<String, Object> checkPlayer(String domainName, String sessionKey,
			int playerId) {
		try {
			log.info("Fro check player we have domain name "+domainName+" session key"+sessionKey +" player id = "+playerId);
			HashMap<String, Object> requestesParameters = new HashMap<String, Object>();
			requestesParameters.put("session_key", sessionKey);
			String url = domainName+UrlCall.API_MY_ACCOUNT;
			String respone = WebSericeCalling.sendPost(requestesParameters, url, null);
			if(respone != null){
				Map<String, Object> value = new Gson().fromJson(
						respone, new TypeToken<HashMap<String, Object>>() {}.getType()
					);
				Map<String, Object> data = new Gson().fromJson(
						value.get("data").toString(), new TypeToken<HashMap<String, Object>>() {}.getType()
					);
				Map<String, Object> retMap = new Gson().fromJson(
						data.get("user_balance").toString(), new TypeToken<HashMap<String, Object>>() {}.getType()
					);
				log.info("get response from api "+value);
				return retMap;
				
			}else{
				return null;
			}
		} catch (Exception e) {
			log.error("failed to get Seesion ", e);
			e.printStackTrace();
		}

		return null;

	}

	public static void main(String[] args) {
		checkPlayer("http://159.203.109.94/tp-api", "ec21b8d4ec52017bbc7c453a0cd33597", 18);
	}

	public static String getPlayerIdByName(int playerId) {
		// TODO Auto-generated method stub
		return null;
	}
}
