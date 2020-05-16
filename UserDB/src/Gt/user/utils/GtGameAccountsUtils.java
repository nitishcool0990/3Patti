package Gt.user.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.spi.TransactionalWriter;

import org.hibernate.Session;
import org.hibernate.Transaction;

import Gt.user.hibernateMapping.GtGameAccounts;
import Gt.user.hibernateMapping.GtGameAccountsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GtGameAccountsUtils {
	
	public static Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");
	private static GtGameAccountsDAO dao = new GtGameAccountsDAO();
	public  static final String IS_REG = "reg";
	public static final String IS_UNREG = "unreg";
	
	public static GtGameAccounts findGameAccounts(int playerID,String roomName){
		GtGameAccounts acc =null;
		Transaction tx = null;
		Session session = null;
		
		try{
			log.info("finding game accounts with playerId = " + playerID + " roomName = " + roomName );
			session = dao.getSession();
			tx = session.beginTransaction();
			List<GtGameAccounts> accounts = dao.findByPlayerIDAndCRoom(playerID, roomName);
			if(accounts!=null && !accounts.isEmpty()){
				acc = accounts.get(0);
				log.info("returning GtGameAccounts instance " + acc.toString());
			}
			tx.commit();
		}catch(Exception e){
			e.printStackTrace();
			tx.rollback();
			log.error("findGameAccount",e);
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		
		
		return acc;
	}
	
	public static GtGameAccounts createGameAccounts(int playerID,String roomName){
		GtGameAccounts acc  = findGameAccounts(playerID, roomName);
		Session session = null;
		Transaction tx = null;
		try{
			log.info("creating new gtGameAccount entry with playerId = " + playerID + " roomName = " + roomName);
			session = dao.getSession();
			tx = session.beginTransaction();
			if(acc ==null){
				acc = new GtGameAccounts();
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				acc.setUserId(playerID);
				acc.setChips(0.0);
				acc.setChipsInPlay(0.0);
				acc.setRoomName(roomName);
				acc.setStatus(IS_UNREG);
				acc.setModifiedDate(timestamp);
				dao.save(acc);
				tx.commit();
			}
		}catch(Exception e){
			System.out.println("save game account error");
			e.printStackTrace();
			log.error("failed to create",e);
			tx.rollback();
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		
		return acc;

	}
	

	
	public static void saveGameAccount(int playerId, String roomName, double chips, double chipsInPlay,String reg){
		Session session = null;
		Transaction tx = null;
		
		try{
			log.info("saving GtGameAccount instance with playerId = " + playerId + " roomName = " + roomName + " total chips = " + chips + " chips in play = " + chipsInPlay);
			session = dao.getSession();
			tx = session.beginTransaction();
			GtGameAccounts account = new GtGameAccounts();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			account.setChips(chips);
			account.setChipsInPlay(chipsInPlay);
			account.setRoomName(roomName);
			account.setStatus(reg);
			account.setUserId(playerId);
			account.setModifiedDate(timestamp);
			dao.save(account);
			tx.commit();
		}catch(Exception e){
			System.out.println("Game Account save error");
			e.printStackTrace();
			log.error("failed to save",e);
			tx.rollback();
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		
		
		
		
	}

	public static void updateGameAccount(GtGameAccounts gameAccount) {
		Session session = null;
		Transaction tx = null;
		
		try{
			log.info("updating GtGameAccount instance " + gameAccount.toString());
			session = dao.getSession();
			tx = session.beginTransaction();
			dao.save(gameAccount);
			tx.commit();
		}catch(Exception e){
			System.out.println("Save game accoutn error");
			e.printStackTrace();
			log.error("failed to update",e);
			tx.rollback();
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		
	}
	
	public static ArrayList<HashMap<String, String>> getPlayerCount(List<String> tableNames) {
		Session session = null;
		Transaction tx = null;
		
		try{
			log.info("Get player count for tableName " + tableNames.toString());
			session = dao.getSession();
			tx = session.beginTransaction();
			ArrayList<HashMap<String, String>> listMap = dao.getPlayerCountRoomWise(tableNames);
			tx.commit();
			return listMap;
		}catch(Exception e){
			log.info("getPlayerCount error");
			e.printStackTrace();
			log.error("failed to update",e);
			tx.rollback();
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		GtGameAccountsUtils dao =new GtGameAccountsUtils();
		ArrayList<String> tableNames = new ArrayList<String>();
		tableNames.add("simple#7#1");
		tableNames.add("simple#7#2");
		dao.getPlayerCount(tableNames);
	}
	
	
	
	
}
