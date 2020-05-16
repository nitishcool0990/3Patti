package utils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import HibernateMapping.GtGameLogGameActivity;
import HibernateMapping.GtGameLogGameActivityDAO;

public class GtGameLogGameActivityUtils {
	private static GtGameLogGameActivityDAO dao = new GtGameLogGameActivityDAO();

	public static void updateGtGameLogGameActivity(GtGameLogGameActivity gtGameLogGameActivity){
		Session session = null;
		Transaction tx = null;
		
		try {
			session = dao.getSession();
			tx = session.beginTransaction();
			dao.save(gtGameLogGameActivity);
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
