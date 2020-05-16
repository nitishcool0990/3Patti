package Gt.HibernateMapping;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * GtGameProp entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see Gt.HibernateMapping.GtGameProp
 * @author MyEclipse Persistence Tools
 */
public class GtGamePropDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");
	// property constants
	public static final String GAME_CONFIG_ID = "gameConfigId";
	public static final String PROP_NAME = "propName";
	public static final String PROP_VALUE = "propValue";
	public static final String PROP_TYPE = "propType";

	public void save(GtGameProp transientInstance) {
		log.debug("saving GtGameProp instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(GtGameProp persistentInstance) {
		log.debug("deleting GtGameProp instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public GtGameProp findById(java.lang.Long id) {
		log.debug("getting GtGameProp instance with id: " + id);
		try {
			GtGameProp instance = (GtGameProp) getSession().get("Gt.HibernateMapping.GtGameProp", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(GtGameProp instance) {
		log.debug("finding GtGameProp instance by example");
		try {
			List results = getSession().createCriteria("Gt.HibernateMapping.GtGameProp").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding GtGameProp instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from GtGameProp as model where model." + propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByGameConfigId(Object gameConfigId) {
		return findByProperty(GAME_CONFIG_ID, gameConfigId);
	}

	public List findByPropName(Object propName) {
		return findByProperty(PROP_NAME, propName);
	}

	public List findByPropValue(Object propValue) {
		return findByProperty(PROP_VALUE, propValue);
	}

	public List findByPropType(Object propType) {
		return findByProperty(PROP_TYPE, propType);
	}

	public List findAll() {
		log.debug("finding all GtGameProp instances");
		try {
			String queryString = "from GtGameProp";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public GtGameProp merge(GtGameProp detachedInstance) {
		log.debug("merging GtGameProp instance");
		try {
			GtGameProp result = (GtGameProp) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(GtGameProp instance) {
		log.debug("attaching dirty GtGameProp instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(GtGameProp instance) {
		log.debug("attaching clean GtGameProp instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public GtGameProp getPropertyValue(int configId,String PropType){
		String queryString = "from GtGameProp as model where model.propName = ? and model.gameConfigId= ?";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setParameter(0, PropType);
		queryObject.setParameter(1, configId);
		if(queryObject.list().isEmpty()){
			return null;
		}else{
			return (GtGameProp) queryObject.list().get(0);
		}
		
	}
}



