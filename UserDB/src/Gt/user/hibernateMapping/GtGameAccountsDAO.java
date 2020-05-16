package Gt.user.hibernateMapping;

import java.util.ArrayList;
import java.util.HashMap;
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
 * GtGameAccounts entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see Gt.user.hibernateMapping.GtGameAccounts
 * @author MyEclipse Persistence Tools
 */
public class GtGameAccountsDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger("db.ThreePattiDbUtil");

	public void save(GtGameAccounts transientInstance) {
		log.debug("saving GtGameAccounts instance");
		try {
			getSession().saveOrUpdate(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(GtGameAccounts persistentInstance) {
		log.debug("deleting GtGameAccounts instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public GtGameAccounts findById(java.lang.Long id) {
		log.debug("getting GtGameAccounts instance with id: " + id);
		try {
			GtGameAccounts instance = (GtGameAccounts) getSession().get("Gt.user.hibernateMapping.GtGameAccounts", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(GtGameAccounts instance) {
		log.debug("finding GtGameAccounts instance by example");
		try {
			List results = getSession().createCriteria("Gt.user.hibernateMapping.GtGameAccounts")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding GtGameAccounts instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from GtGameAccounts as model where model." + propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all GtGameAccounts instances");
		try {
			String queryString = "from GtGameAccounts";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public GtGameAccounts merge(GtGameAccounts detachedInstance) {
		log.debug("merging GtGameAccounts instance");
		try {
			GtGameAccounts result = (GtGameAccounts) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(GtGameAccounts instance) {
		log.debug("attaching dirty GtGameAccounts instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(GtGameAccounts instance) {
		log.debug("attaching clean GtGameAccounts instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
    public List findByPlayerIDAndCRoom(int playerId,String roomName) {
        log.debug("finding GtGameAccounts instance with playerID: " + playerId + ", roomName: " + roomName);
        try {
            String queryString = "from GtGameAccounts as model where model.userId= ? and model.roomName=?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, playerId);
            queryObject.setParameter(1, roomName);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }
    
    public ArrayList<HashMap<String, String>> getPlayerCountRoomWise(List<String> tableNames){
    log.debug("Finding all playecunt room wise");
    	try{
    		//SELECT room_name, COUNT(user_id) FROM `gt_game_accounts` WHERE room_name IN ('simple#7#1','simple#7#2') AND STATUS = "reg" GROUP BY room_name;
    		String queryString = "SELECT new map(roomName as roomName, COUNT(userId) as playerCount) FROM GtGameAccounts WHERE room_name IN (:list) AND STATUS = 'reg' GROUP BY room_name";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameterList("list", tableNames);
           ArrayList<HashMap<String, String>> map = (ArrayList<HashMap<String, String>>) queryObject.list();
           
            return map;
    	}catch(Exception e){
    		log.error("Getting some error during playercount room wise",e);
//    		throw e;
    		return null;
    	}
    }
    public static void main(String[] args) {
		GtGameAccountsDAO dao =new GtGameAccountsDAO();
		ArrayList<String> tableNames = new ArrayList<String>();
		tableNames.add("simple#7#1");
		tableNames.add("simple#7#2");
		dao.getPlayerCountRoomWise(tableNames);
	}
}