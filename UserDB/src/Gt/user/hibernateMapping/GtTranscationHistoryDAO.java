package Gt.user.hibernateMapping;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * GtTranscationHistory entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see Gt.user.hibernateMapping.GtTranscationHistory
 * @author MyEclipse Persistence Tools
 */
public class GtTranscationHistoryDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");
	// property constants
	public static final String USER_ID = "userId";
	public static final String CHIP_TYPE = "chipType";
	public static final String GAME_TABLE_ID = "gameTableId";
	public static final String CHIPS = "chips";
	public static final String CR_DR = "crDr";
	public static final String REMARKS = "remarks";

	public void save(GtTranscationHistory transientInstance) {
		log.debug("saving GtTranscationHistory instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(GtTranscationHistory persistentInstance) {
		log.debug("deleting GtTranscationHistory instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public GtTranscationHistory findById(java.lang.Long id) {
		log.debug("getting GtTranscationHistory instance with id: " + id);
		try {
			GtTranscationHistory instance = (GtTranscationHistory) getSession()
					.get("Gt.user.hibernateMapping.GtTranscationHistory", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(GtTranscationHistory instance) {
		log.debug("finding GtTranscationHistory instance by example");
		try {
			List results = getSession().createCriteria("Gt.user.hibernateMapping.GtTranscationHistory")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding GtTranscationHistory instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from GtTranscationHistory as model where model." + propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}

	public List findByChipType(Object chipType) {
		return findByProperty(CHIP_TYPE, chipType);
	}

	public List findByGameTableId(Object gameTableId) {
		return findByProperty(GAME_TABLE_ID, gameTableId);
	}

	public List findByChips(Object chips) {
		return findByProperty(CHIPS, chips);
	}

	public List findByCrDr(Object crDr) {
		return findByProperty(CR_DR, crDr);
	}

	public List findByRemarks(Object remarks) {
		return findByProperty(REMARKS, remarks);
	}

	public List findAll() {
		log.debug("finding all GtTranscationHistory instances");
		try {
			String queryString = "from GtTranscationHistory";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public GtTranscationHistory merge(GtTranscationHistory detachedInstance) {
		log.debug("merging GtTranscationHistory instance");
		try {
			GtTranscationHistory result = (GtTranscationHistory) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(GtTranscationHistory instance) {
		log.debug("attaching dirty GtTranscationHistory instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(GtTranscationHistory instance) {
		log.debug("attaching clean GtTranscationHistory instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}