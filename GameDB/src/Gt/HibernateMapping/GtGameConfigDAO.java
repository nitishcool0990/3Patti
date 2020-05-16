package Gt.HibernateMapping;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * GtGameConfig entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see Gt.HibernateMapping.GtGameConfig
 * @author MyEclipse Persistence Tools
 */
public class GtGameConfigDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");
	// property constants
	public static final String CHIP_TYPE = "chipType";
	public static final String GAME_VARIANT = "gameVariant";
	public static final String MIN_PLAYERS = "minPlayers";
	public static final String MAX_PLAYERS = "maxPlayers";
	public static final String MIN_ROOM = "minRoom";
	public static final String MAX_ROOM = "maxRoom";
	public static final String STATUS = "status";

	public void save(GtGameConfig transientInstance) {
		log.debug("saving GtGameConfig instance");
		try {
			getSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(GtGameConfig persistentInstance) {
		log.debug("deleting GtGameConfig instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public GtGameConfig findById(java.lang.Integer id) {
		log.debug("getting GtGameConfig instance with id: " + id);
		try {
			GtGameConfig instance = (GtGameConfig) getSession().get("Gt.HibernateMapping.GtGameConfig", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(GtGameConfig instance) {
		log.debug("finding GtGameConfig instance by example");
		try {
			List results = getSession().createCriteria("Gt.HibernateMapping.GtGameConfig").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding GtGameConfig instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from GtGameConfig as model where model." + propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByChipType(Object chiType) {
		return findByProperty(CHIP_TYPE, chiType);
	}

	public List findByGameVariant(Object gameVariant) {
		return findByProperty(GAME_VARIANT, gameVariant);
	}

	public List findByMinPlayers(Object minPlayers) {
		return findByProperty(MIN_PLAYERS, minPlayers);
	}

	public List findByMaxPlayers(Object maxPlayers) {
		return findByProperty(MAX_PLAYERS, maxPlayers);
	}

	public List findByMinRoom(Object minRoom) {
		return findByProperty(MIN_ROOM, minRoom);
	}

	public List findByMaxRoom(Object maxRoom) {
		return findByProperty(MAX_ROOM, maxRoom);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all GtGameConfig instances");
		try {
			String queryString = "from GtGameConfig";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public GtGameConfig merge(GtGameConfig detachedInstance) {
		log.debug("merging GtGameConfig instance");
		try {
			GtGameConfig result = (GtGameConfig) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(GtGameConfig instance) {
		log.debug("attaching dirty GtGameConfig instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(GtGameConfig instance) {
		log.debug("attaching clean GtGameConfig instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}