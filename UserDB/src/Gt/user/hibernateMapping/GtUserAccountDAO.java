package Gt.user.hibernateMapping;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * GtUserAccount entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see Gt.user.hibernateMapping.GtUserAccount
 * @author MyEclipse Persistence Tools
 */
public class GtUserAccountDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");

	public void save(GtUserAccount transientInstance) {
		log.debug("saving GtUserAccount instance");
		try {
			getSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(GtUserAccount persistentInstance) {
		log.debug("deleting GtUserAccount instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public GtUserAccount findById(Gt.user.hibernateMapping.GtUserAccount id) {
		log.debug("getting GtUserAccount instance with id: " + id);
		try {
			GtUserAccount instance = (GtUserAccount) getSession().get("Gt.user.hibernateMapping.GtUserAccount", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(GtUserAccount instance) {
		log.debug("finding GtUserAccount instance by example");
		try {
			List results = getSession().createCriteria("Gt.user.hibernateMapping.GtUserAccount")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding GtUserAccount instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from GtUserAccount as model where model." + propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public GtUserAccount findByPlayerId(int playerId){
		log.debug("finding GtUserAccount instance with property: UserId  value: " + playerId);
		try {
			String queryString = "from GtUserAccount as model where model.userId= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, playerId);
			queryObject.uniqueResult();
			return (GtUserAccount) queryObject.list().get(0);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all GtUserAccount instances");
		try {
			String queryString = "from GtUserAccount";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public GtUserAccount merge(GtUserAccount detachedInstance) {
		log.debug("merging GtUserAccount instance");
		try {
			GtUserAccount result = (GtUserAccount) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(GtUserAccount instance) {
		log.debug("attaching dirty GtUserAccount instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(GtUserAccount instance) {
		log.debug("attaching clean GtUserAccount instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}