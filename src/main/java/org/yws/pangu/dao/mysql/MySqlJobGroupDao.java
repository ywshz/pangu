package org.yws.pangu.dao.mysql;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yws.pangu.domain.JobGroup;

@Repository
public class MySqlJobGroupDao {
	@Autowired
	private SessionFactory sessionFacotry;

	@SuppressWarnings("unchecked")
	public List<JobGroup> list(Integer parentId) {
		Session session = sessionFacotry.getCurrentSession();
		Criteria criteria = session.createCriteria(JobGroup.class);
		criteria.add(Restrictions.eq("parent", parentId));

		return criteria.list();
	}

	public JobGroup getRootGroup(String owner) {
		Session session = sessionFacotry.getCurrentSession();
		Criteria criteria = session.createCriteria(JobGroup.class);
		criteria.add(Restrictions.eq("owner", owner));
		criteria.add(Restrictions.isNull("parent"));

		return (JobGroup) criteria.uniqueResult();
	}

}