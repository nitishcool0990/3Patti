package utils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import HibernateMapping.GtGameLogPlayerJoined;
import HibernateMapping.GtGameLogPlayerJoinedDAO;

public class GtGameLogPlayerJoinedUtils {
	private static GtGameLogPlayerJoinedDAO dao = new GtGameLogPlayerJoinedDAO();

	public static void updateGtGameLogPlayerJoined(GtGameLogPlayerJoined gtGameLogPlayerJoined){
		Session session = null;
		Transaction tx = null;
		
		try {
			session = dao.getSession();
			tx = session.beginTransaction();
			dao.save(gtGameLogPlayerJoined);
			
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
}
