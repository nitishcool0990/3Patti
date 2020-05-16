package Gt.utils;

import Gt.HibernateMapping.GtGameConfig;
import Gt.HibernateMapping.GtGameConfigDAO;
import java.util.List;

import javax.sql.rowset.spi.TransactionalWriter;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GtGameConfigUtils
{
  private static GtGameConfigDAO dao = new GtGameConfigDAO();
  public static final String GC_NEW = "new";
  public static final String GC_CREATED = "created";
  public static final String GC_HIDE = "hide";
  public static final String GC_BAD = "bad";
  
  public static Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");
  
  public static List<GtGameConfig> getAllConfig()
  {
	Session session = null;
	 Transaction txn = null;
	List<GtGameConfig> gameConfigList = null;
	try{
		session = dao.getSession();
		txn = session.beginTransaction();
		gameConfigList =  dao.findAll();
		txn.commit();
		log.info("getting all GtGameConfig ");
	}catch(Exception e){
		e.printStackTrace();
		log.error("GtGameConfig ",e);
		if (session != null) {
	        txn.rollback();
	      }
	}finally{
		if(session != null && session.isOpen()){
			session.close();
		}
	}
    
	return gameConfigList;
  }
  
  public static void saveGameConfigTable(GtGameConfig gtGameConfig)
  {
    Session session = null;
    Transaction txn = null;
    try
    {
      log.info("saving GtGameConfig instance" + gtGameConfig.toString());
      session = dao.getSession();
      txn = session.beginTransaction();
      dao.save(gtGameConfig);
      txn.commit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      log.error("saveGameConfigTable",e);
      if (session != null) {
        txn.rollback();
      }
    }
    finally
    {
    	if(session != null && session.isOpen()){
			session.close();
		}
    }
  }
  
  public static GtGameConfig getGameConfigById(int configID){
	  Session session = null;
	  Transaction txn = null;
	  GtGameConfig gameConfig =null;
	  try{
		  log.info("getting GtGameConfig by config Id = " +configID);
		  session = dao.getSession();
		  txn = session.beginTransaction();
		  gameConfig = dao.findById(configID);
		  txn.commit();
	  }catch(Exception e){
		  e.printStackTrace();
		  log.error("getGameConfigById",e);
		  txn.rollback();
	  }finally {
		  if(session != null && session.isOpen()){
				session.close();
			}
	}
	  log.info("returning GtGameConfig instance " + gameConfig.toString());
	return gameConfig;
  }
  
}
