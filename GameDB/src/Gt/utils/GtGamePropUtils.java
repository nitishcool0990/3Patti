package Gt.utils;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import Gt.HibernateMapping.GtGameProp;
import Gt.HibernateMapping.GtGamePropDAO;
import Gt.HibernateMapping.GtGameTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GtGamePropUtils {
	public static Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");
	private static GtGamePropDAO dao = new GtGamePropDAO();
	public static List<GtGameProp> getTablePropsByConfigId(Object cid) {

		Session session = null;
		 Transaction txn = null;
		List<GtGameProp> gamePropList = null;
		
		try{
			session = dao.getSession();
			txn = session.beginTransaction();
			gamePropList = dao.findByGameConfigId(cid);
			txn.commit();
			//log.info("getting GtGameProp by config Id = " + cid.toString() );
		}catch(Exception e){
			e.printStackTrace();
			txn.rollback();
			log.error("getGameConfigById" + e);
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		
		return gamePropList;
	}
	
	public static GtGameProp findByProperty(int configId,String propertyName){
		Session session=null;
		GtGameProp prop = null;
		 Transaction txn = null;
		try{
			log.info("getting Game Property value by property name = " + propertyName + " and configId = " + configId);
			session = dao.getSession();
			txn = session.beginTransaction();
			prop =  dao.getPropertyValue(configId, propertyName);
			
			txn.commit();
		}catch(Exception e){
			e.printStackTrace();
			txn.rollback();
			log.error("findByProperty",e);
		}finally {
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		//log.info("returning GtGameProp instance " + prop.toString());
		return prop;
	}
	
	//fetch all data acc to gameconfigid from gt_game_prop
}
