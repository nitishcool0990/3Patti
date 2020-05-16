package Gt.user.utils;

import java.util.HashMap;
import java.util.Map;


import org.hibernate.Session;
import org.hibernate.Transaction;


import Gt.common.ChipInfo;
import Gt.common.UrlCall;
import Gt.test.WebSericeCalling;
import Gt.user.hibernateMapping.GtUserAccount;
import Gt.user.hibernateMapping.GtUserAccountDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
public class GtUserAccountUtils {
	private static GtUserAccountDAO dao = new GtUserAccountDAO();
	public static final String DUMMY = "dummy_amount";
	public static final String REAL = "real_amount";
	public static Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");
	private static final String SERVER_PRIVATE_KEY = "DC892D7D7EE2EE48F2426030C3D0A76F59400D851562165D8F283B4E3AEA5C82FF065847B49B4CC27F3DA907D7ADC3DAE7F643A9FB9133E5A1EC2EE549A5D9A4";
	
	public static GtUserAccount findByPlayerId(int playerId){
		Session session = null;	
		Transaction tx = null;
		GtUserAccount gtUserAcc = null;
		try{
			log.info("Finding User Account of playerId = " + playerId);
			session = dao.getSession();
			tx = session.beginTransaction();
			gtUserAcc = dao.findByPlayerId(playerId);
			tx.commit();
		}catch(Exception e){
			log.error("failed to find player",e);
			e.printStackTrace();
			tx.rollback();
		}finally {
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		
		log.info("returning GtUserAccount instance " + gtUserAcc.toString());
		return gtUserAcc;
	}

	public static synchronized HashMap<String, Integer> crDR(int playerId,ChipInfo chipInfo,int gameTableId, String remarks,String sessionKey,String domainName,boolean isCr,int gameId,int apiActionType,boolean isPrivate, int gameVariant){
		log.info("Inside GtUSerACcountUtils for credit/debit transaction of" + " playerId =  " + playerId + " chip type = " + chipInfo.getChipType() + " game Table id = " +  gameTableId + " chips = "+ chipInfo.getChipAmount() + " isCredit = " +  isCr + " reamrks = " + remarks );

		HashMap<String, Object> requestesParameters = new HashMap<String, Object>();
		HashMap<String, Integer> orderDetails = new HashMap<String, Integer>();
		int orderId = 0;
		Integer transactionId = 0;
		String url ="";
		if(isCr){
			
			String chipType= chipInfo.getChipType();
			int chipTypeIntValue=0;								// 0 - Real, 3 - dummy
			if(chipType.equals(GtUserAccountUtils.DUMMY)){
				chipTypeIntValue =3;
			}
			int isPrivateIntVal = 0;
			if(isPrivate){
				isPrivateIntVal= 1;
			}
			requestesParameters.put("user_id", playerId);
			requestesParameters.put("amount", chipInfo.getChipAmount());
			requestesParameters.put("cash_type", chipTypeIntValue);
			requestesParameters.put("plateform", Integer.parseInt(chipInfo.getPlateform()));
			requestesParameters.put("source", Integer.parseInt(chipInfo.getSource()));				//place from where this req coming like take seat or leave seat
			requestesParameters.put("source_id", Integer.parseInt(chipInfo.getSourceId()));
			url = domainName + UrlCall.API_DEPOSIT;
			try{
				String response = WebSericeCalling.sendPost(requestesParameters, url, SERVER_PRIVATE_KEY);
				if (response != null) {
					Map<String, Object> value = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>() {
					}.getType());
					String a = value.get("data").toString();
					Map<String, Object> data = new Gson().fromJson(a, new TypeToken<HashMap<String, Object>>() {
					}.getType());
					Double order = (Double) data.get("order_id");
					orderId = order.intValue();
					transactionId = GtTransactionHistoryUtils.transcationHistory(playerId, chipInfo.getChipType(), gameTableId, chipInfo.getChipAmount(), apiActionType, remarks,domainName,SERVER_PRIVATE_KEY,gameId,orderId,isPrivateIntVal,0, gameVariant);// 0 running balance is zero as per ashish
					orderDetails.put("orderId",orderId);
					orderDetails.put("transId", transactionId);
				}
			}catch(Exception e){
				log.debug("",e);
//				return -1;
				return orderDetails;
			}
			
		}else{
			String chipType= chipInfo.getChipType();
			int chipTypeIntValue=0;
			if(chipType.equals(GtUserAccountUtils.DUMMY)){				// 0 - Real, 3 - dummy
				chipTypeIntValue =3;
			}
			int isPrivateIntVal = 0;
			if(isPrivate){
				isPrivateIntVal= 1;
			}
			requestesParameters.put("session_key", sessionKey);
			requestesParameters.put("amount", chipInfo.getChipAmount());
			requestesParameters.put("cash_type", chipTypeIntValue);
			requestesParameters.put("plateform", Integer.parseInt(chipInfo.getPlateform()));
			requestesParameters.put("source", Integer.parseInt(chipInfo.getSource()));//place from where this req coming like take seat or leave seat
			requestesParameters.put("source_id", Integer.parseInt(chipInfo.getSourceId()));
			requestesParameters.put("withdraw_method", 3);
			url = domainName + UrlCall.API_WITHDREW;
			
			try{
				String response = WebSericeCalling.sendPost(requestesParameters, url, null);
				if (response != null) {
					Map<String, Object> value = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>() {
					}.getType());
					String a = value.get("data").toString();
					Map<String, Object> data = new Gson().fromJson(a, new TypeToken<HashMap<String, Object>>() {
					}.getType());
					Double order = (Double) data.get("order_id");
					orderId = order.intValue();
					transactionId = GtTransactionHistoryUtils.transcationHistory(playerId, chipInfo.getChipType(), gameTableId, chipInfo.getChipAmount(),apiActionType, remarks,domainName,SERVER_PRIVATE_KEY,gameId,orderId,isPrivateIntVal,chipInfo.getChipAmount(), gameVariant);
					orderDetails.put("orderId",orderId);
					orderDetails.put("transId", transactionId);
				}
			}catch(Exception e){
				log.debug("",e);
				//return -1;
				return orderDetails;
			}
		}
		
		
		//return orderId;
		log.info("orderDetails returned : "+orderDetails);
		return orderDetails;

	}

	/*public static void updateUserAccount(GtUserAccount accounts) {
		Session session = null;
		Transaction tx = null;
		
		try{
			log.info("updating user account balance " + accounts.toString());
			session = dao.getSession();
			tx = session.beginTransaction();
			dao.save(accounts);
			tx.commit();
		}catch(Exception e){
			log.debug("",e);
			e.printStackTrace();
			tx.rollback();
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		
		
	}*/
	
		
			}
