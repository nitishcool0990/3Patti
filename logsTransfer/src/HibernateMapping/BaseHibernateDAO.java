package HibernateMapping;

import org.hibernate.Session;

import HibernateMapping.IBaseHibernateDAO;
import HibernateSession.HibernateSessionFactory;


/**
 * Data access object (DAO) for domain model
 * @author MyEclipse Persistence Tools
 */
public class BaseHibernateDAO implements IBaseHibernateDAO {
	
	public Session getSession() {
		return HibernateSessionFactory.getSession();
	}
	
}