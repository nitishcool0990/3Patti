package Gt.utils;

import Gt.HibernateMapping.GtGameProp;
import Gt.HibernateMapping.GtGameTable;
import Gt.HibernateMapping.GtGameTableDAO;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class GtGameTableUtils
{
  private static GtGameTableDAO dao = new GtGameTableDAO();
  public static final String GT_NEW = "new";
  public static final String GT_CREATED = "created";
  public static final String GT_ACTIVE = "Active";
  public static final String GT_INACTIVE = "InActive";
  
  public static Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");
  
  public static void saveGameTable(GtGameTable gtGameTable)
  {
    Session session = null;
    Transaction txn = null;
    try
    {
      log.info("saving GameTable instance " + gtGameTable.toString());
      session = dao.getSession();
      txn = session.beginTransaction();
      dao.save(gtGameTable);
      txn.commit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      log.error("saveGameTable",e);
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
  
  public static List<GtGameTable> getActiveGameTables(int gameConfigId)
  {
	  
	  Session session = null;
	  Transaction txn = null;
		List<GtGameTable> gameTableList = null;
		
		try{
			log.info("getting active table where configId = " + gameConfigId);
			session = dao.getSession();
			txn = session.beginTransaction();
			gameTableList = dao.findByGameConfigId(gameConfigId);
			txn.commit();
		}catch(Exception e){
			e.printStackTrace();
			log.error("getActiveGameTables",e);
			txn.rollback();
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
	  
		log.info("returning List of GtGameTable instances " + gameTableList.toString());
    return gameTableList;
  }
  
  public static GtGameTable getGameTable(String roomName){
	  GtGameTable gameTable = null;
	  List<GtGameTable> table = null;
	  Session session = null;
	  Transaction txn = null;
	  
	  try{
		  log.info("getting GameTable by roomName = " + roomName);
		  session = dao.getSession();
		  txn = session.beginTransaction();
		  table = dao.findByTableName(roomName);
		  if(table!=null && !table.isEmpty())
		  {
			  gameTable = table.get(0);
		  }
		  txn.commit();
	  }catch(Exception e){
		  e.printStackTrace();
		  log.error("getGameTable",e);
		  txn.rollback();
	  }finally{
		  if(session != null && session.isOpen()){
				session.close();
			}
	  }
	 
	  log.info("returning GtGameTable instance " + gameTable.toString());
	  return gameTable;
  }
  
  public static List<GtGameTable> getAllActiveGameTables()
  {
	  
	  Session session = null;
	  Transaction txn = null;
		List<GtGameTable> gameTableList = null;
		
		try{
			log.info("getting active table where configId = ");
			session = dao.getSession();
			txn = session.beginTransaction();
			gameTableList = dao.findAllActiveGameTable();
			txn.commit();
		}catch(Exception e){
			e.printStackTrace();
			log.error("getActiveGameTables",e);
			txn.rollback();
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
	  
		log.info("returning List of GtGameTable instances " + gameTableList.toString());
    return gameTableList;
  }
  
}
