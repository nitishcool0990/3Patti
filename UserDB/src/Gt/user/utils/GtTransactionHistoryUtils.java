package Gt.user.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.hibernate.Transaction;
import Gt.common.UrlCall;
import Gt.test.WebSericeCalling;
import Gt.user.hibernateMapping.GtTranscationHistory;
import Gt.user.hibernateMapping.GtTranscationHistoryDAO;

public class GtTransactionHistoryUtils {
	private static GtTranscationHistoryDAO dao = new GtTranscationHistoryDAO();
	public static Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");

	public static int transcationHistory(int playerId, String chipType, int gameTableId, double chips, int actionType,
			String remarks, String domainName, String sessionKey, int gameId, int orderId,int isPrivate,double runningBalance, int gameVariant) {
		// insert in gt_transaction_history
		Session session = null;
		Transaction tx = null;
		try {
			/*log.info("inserting new transaction record in GtTransactionHistory. playerId =  " + playerId + " chip type = " + chipType + " game Table id = " +  gameTableId + " chips = "+ chips + " credit/debit = " +crDr + "reamrks = " + remarks );
			session = dao.getSession();
			tx = session.beginTransaction();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			GtTranscationHistory tranx = new GtTranscationHistory();
			tranx.setUserId(playerId);
			tranx.setChips(chips);
			tranx.setChipType(chipType);
			tranx.setCrDr(crDr);
			tranx.setGameTableId(gameTableId);
			tranx.setRemarks(remarks);
			tranx.setModifiedDate(timestamp);
			dao.save(tranx);
			tx.commit();*/
			
			int chipTypeIntValue = 0;						// 0 - Real money , 1 - Dummy
			
			if(chipType.equals(GtUserAccountUtils.DUMMY)){
				 chipTypeIntValue = 1;
			}
			
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			 map.put("user_id", playerId);
			 map.put("chip_type",chipTypeIntValue );
			 map.put("game_table_id", gameTableId);
			 map.put("chips", chips);
			 map.put("action_type",actionType);
			 map.put("remarks",remarks);
			 map.put("game_id", gameId);
			 map.put("order_id", orderId);
			 map.put("is_private", isPrivate);
			 map.put("running_balance", runningBalance);
			 map.put("game_variant", gameVariant);
			 String url = domainName + UrlCall.API_TRANSACTION_HISTORY;
			 log.info(map.toString());
			 log.info("transaction history at " + url);
			 String response = WebSericeCalling.sendPost(map, url, sessionKey);
			 log.info("response from update api : "+response);
			 
			 Map<String, Object> value = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>() {}.getType());
			 log.info("value : "+value);
			 Map<String, Object> data = new Gson().fromJson(value.get("data").toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
			 log.info("data : "+data);			
			 Integer transactionId = ( (Double)data.get("id") ).intValue();
			 log.info("id returned : "+transactionId);
			 return transactionId;
			 
		} catch (Exception e) {
			System.out.println("save transaction error during save"+e);
			e.printStackTrace();
			log.error("failed to insert transaction history",e);
			return -1;
			//tx.rollback();
		} finally {
			if(session != null && session.isOpen()){
				session.close();
			}
		}
	}
	
	public static void updateGameVariant(HashMap<Integer, Integer> playerMap, int gameVariant, String domainName, String sessionKey) {
		String url = domainName + UrlCall.API_TRANSACTION_UPDATE;
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<Integer> transactionList = new ArrayList<Integer>(playerMap.values());
		log.info("transaction list sent for updation : "+transactionList);
		map.put("transaction_ids", transactionList);
		map.put("game_variant", gameVariant);
		try {
			log.info("sending update request at url : "+url);
			String response = WebSericeCalling.sendPost(map, url, sessionKey);
			log.info("response of update : "+response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("Exception in updateGameVariant in GtTransactionHistoryUtils\n"+e);
			e.printStackTrace();
		}
	}
}
