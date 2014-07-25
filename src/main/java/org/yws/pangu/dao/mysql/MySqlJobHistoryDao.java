package org.yws.pangu.dao.mysql;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yws.pangu.domain.JobHistory;

@Repository
public class MySqlJobHistoryDao {
	@Autowired
	private SessionFactory sessionFacotry;

	@SuppressWarnings("unchecked")
	public List<JobHistory> list(Long jobId) {
		Session session = sessionFacotry.getCurrentSession();

		Criteria criteria = session.createCriteria(JobHistory.class);
		criteria.add(Restrictions.eq("jobId", jobId));
		criteria.addOrder(Order.desc("startTime"));
		criteria.setMaxResults(10);
		return criteria.list();
	}
	
}