package HibernateMapping;

import java.util.List;

import org.hibernate.Query;

public class GtGameLogPlayerJoinedDAO extends BaseHibernateDAO {

	public void save(GtGameLogPlayerJoined transientInstance) {
		try {
			getSession().saveOrUpdate(transientInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(GtGameLogPlayerJoined persistentInstance) {
		try {
			getSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List findAll() {
		try {
			String queryString = "from GtGameLogPlayerJoined";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
