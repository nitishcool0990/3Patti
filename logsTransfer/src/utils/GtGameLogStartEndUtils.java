package utils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import HibernateMapping.GtGameLogStartEnd;
import HibernateMapping.GtGameLogStartEndDAO;

public class GtGameLogStartEndUtils {
	
	private static GtGameLogStartEndDAO dao = new GtGameLogStartEndDAO();
	
	public static void updateGtGameLogStartEnd(GtGameLogStartEnd GtGameLogStartEnd){
		Session session = null;
		Transaction tx = null;
		
		try {
			session = dao.getSession();
			tx = session.beginTransaction();
			dao.save(GtGameLogStartEnd);
			
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			
		}finally {
			if(session != null && session.isOpen()){
				session.close();
			}
		}
	}
	
	public static void getLastTableGameId(){
		Session session = null;
		Transaction tx = null;
		
		try {
			session = dao.getSession();
			tx = session.beginTransaction();
			
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
			
		}finally {
			if(session != null && session.isOpen()){
				session.close();
			}
		}
	}

	public static long findTableGameId(Long gameId, Long tableId) {
		Session session = null;
		Transaction tx = null;
		long tableGameId = 0;
		
		try {
			session = dao.getSession();
			tx = session.beginTransaction();
			tableGameId = dao.findTableGameId(gameId,tableId);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}finally{
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		
		return tableGameId;
	}
}
