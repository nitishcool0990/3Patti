package HibernateMapping;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;


public class GtGameLogStartEndDAO extends BaseHibernateDAO {

	public void save(GtGameLogStartEnd transientInstance) {
		try {
			getSession().saveOrUpdate(transientInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(GtGameLogStartEnd persistentInstance) {
		try {
			getSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List findAll() {
		try {
			String queryString = "from GtGameLogStartEnd";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public long findTableGameId(Long gameId, Long tableId) {
		
		long tableGameId = 0;
		try {
			Session session = getSession();
			String queryString = " from GtGameLogStartEnd as model where model.gameId = ? and model.tableId = ?";
			Query query = session.createQuery(queryString);
			query.setParameter(0, gameId);
			query.setParameter(1, tableId);
			
			if(query.list().isEmpty()){
				tableGameId =  0;
			}else{
				GtGameLogStartEnd game = (GtGameLogStartEnd) query.list().get(0);
				tableGameId =  game.getId();
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tableGameId;
		
	}

	
}
