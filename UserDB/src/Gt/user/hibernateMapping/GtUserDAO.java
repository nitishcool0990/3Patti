package Gt.user.hibernateMapping;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * GtUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see Gt.user.hibernateMapping.GtUser
 * @author MyEclipse Persistence Tools
 */
public class GtUserDAO extends BaseHibernateDAO {
	public static Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");
	// property constants
	public static final String USER_NAME = "userName";
	public static final String PASSWORD = "password";
	public static final String EMAIL = "email";
	public static final String PHONE_NO = "phoneNo";
	public static final String GENDER = "gender";
	public static final String STTAUS = "sttaus";

	public void save(GtUser transientInstance) {
		log.debug("saving GtUser instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(GtUser persistentInstance) {
		log.debug("deleting GtUser instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public GtUser findById(java.lang.Long id) {
		log.debug("getting GtUser instance with id: " + id);
		try {
			GtUser instance = (GtUser) getSession().get("Gt.user.hibernateMapping.GtUser", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(GtUser instance) {
		log.debug("finding GtUser instance by example");
		try {
			List results = getSession().createCriteria("Gt.user.hibernateMapping.GtUser").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding GtUser instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from GtUser as model where model." + propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	
	
	public List<GtUser> getByPlayerName(String playerName){
		try {
			log.info("Player NAme = "+playerName);
			Criteria cri =getSession().createCriteria(GtUser.class).add(Restrictions.like("userName", playerName));
			/*String queryString = "from Gt.user.hibernateMapping.GtUser as model where model.userName like '"+playerName+"'";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.uniqueResult();*/
			cri.uniqueResult();
			
			//queryObject.setParameter(0, playerName);
			return  cri.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByUserName(Object userName) {
		return findByProperty(USER_NAME, userName);
	}

	public List findByPassword(Object password) {
		return findByProperty(PASSWORD, password);
	}

	public List findByEmail(Object email) {
		return findByProperty(EMAIL, email);
	}

	public List findByPhoneNo(Object phoneNo) {
		return findByProperty(PHONE_NO, phoneNo);
	}

	public List findByGender(Object gender) {
		return findByProperty(GENDER, gender);
	}

	public List findBySttaus(Object sttaus) {
		return findByProperty(STTAUS, sttaus);
	}

	public List findAll() {
		log.debug("finding all GtUser instances");
		try {
			String queryString = "from GtUser";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public GtUser merge(GtUser detachedInstance) {
		log.debug("merging GtUser instance");
		try {
			GtUser result = (GtUser) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(GtUser instance) {
		log.debug("attaching dirty GtUser instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(GtUser instance) {
		log.debug("attaching clean GtUser instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}