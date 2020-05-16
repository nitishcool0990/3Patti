package Gt.HibernateMapping;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GtDomainNameDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");
	private static final String GAME_NAME = "gameName";
	
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding GtDomainName instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from GtDomainName as model where model." + propertyName + "= ? and model.status=1";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all GtDomainName instances");
		try {
			String queryString = "from GtDomainName";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List findByGame(Object name) {
		return findByProperty(GAME_NAME, name);
	}
	
	
}
