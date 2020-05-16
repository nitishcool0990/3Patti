package Gt.utils;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Gt.HibernateMapping.GtDomainName;
import Gt.HibernateMapping.GtDomainNameDAO;


public class GtDomainNameUtils {
	private static GtDomainNameDAO dao = new GtDomainNameDAO();
	public static Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");
	 
	public static List<GtDomainName> getAllDomains()
	  {
		Session session = null;
		 Transaction txn = null;
		List<GtDomainName> domainNameList = null;
		try{
			session = dao.getSession();
			txn = session.beginTransaction();
			domainNameList =  dao.findAll();
			txn.commit();
			log.info("getting all GtDomainNames ");
			log.info(domainNameList.toString());
		}catch(Exception e){
			e.printStackTrace();
			log.error("GtDomainNames ",e);
			if (session != null) {
		        txn.rollback();
		      }
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
	    
		return domainNameList;
	  }

	public static GtDomainName getTeenPattiDomain() {
		Session session = null;
		 Transaction txn = null;
		 GtDomainName domain = null;
		try{
			session = dao.getSession();
			txn = session.beginTransaction();
			 List<GtDomainName> domains  =  dao.findByGame("teenpatti");
			if(domains != null && domains.size() > 0){
				domain = domains.get(0);
			}
			txn.commit();
			log.info("getting all GtDomainNames ");
			log.info(domains.toString());
		}catch(Exception e){
			e.printStackTrace();
			log.error("GtDomainNames ",e);
			if (session != null) {
		        txn.rollback();
		      }
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		return domain;
	}
	
}
