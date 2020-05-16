package HibernateMapping;

import java.util.List;

import org.hibernate.Query;

public class GtGameLogGameActivityDAO extends BaseHibernateDAO {

	public void save(GtGameLogGameActivity transientInstance) {
		try {
			getSession().saveOrUpdate(transientInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(GtGameLogGameActivity persistentInstance) {
		try {
			getSession().delete(persistentInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List findAll() {
		try {
			String queryString = "from GtGameLogGameActivity";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
}
