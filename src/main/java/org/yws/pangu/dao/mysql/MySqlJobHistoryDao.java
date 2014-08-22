package org.yws.pangu.dao.mysql;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yws.pangu.domain.JobHistory;
import org.yws.pangu.enums.ETimeOrderType;

@Repository
public class MySqlJobHistoryDao {
	@Autowired
	private SessionFactory sessionFacotry;

	@SuppressWarnings("unchecked")
	public List<JobHistory> list(Integer jobId) {
		Session session = sessionFacotry.getCurrentSession();

		Criteria criteria = session.createCriteria(JobHistory.class);
		criteria.add(Restrictions.eq("jobId", jobId));
		criteria.addOrder(Order.desc("startTime"));
		criteria.setMaxResults(10);
		return criteria.list();
	}

	public void save(JobHistory history) {
		Session session = sessionFacotry.getCurrentSession();
		session.save(history);
	}

	public void update(JobHistory history) {
		Session session = sessionFacotry.getCurrentSession();
		session.update(history);
	}

	public JobHistory get(Long historyId) {
		Session session = sessionFacotry.getCurrentSession();
		return (JobHistory) session.get(JobHistory.class, historyId);
	}

	@SuppressWarnings("unchecked")
	public List<JobHistory> list(Date start, Date end) {
		Session session = sessionFacotry.getCurrentSession();
		Criteria criteria = session.createCriteria(JobHistory.class);
		criteria.add(Restrictions.and(Restrictions.ge("startTime", start),
				Restrictions.lt("startTime", end)));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<JobHistory> list(Date start, Date end, ETimeOrderType timeOrderType) {
		Session session = sessionFacotry.getCurrentSession();
		Criteria criteria = session.createCriteria(JobHistory.class);
		criteria.add(Restrictions.and(Restrictions.ge("startTime", start),
				Restrictions.lt("startTime", end)));
		if (timeOrderType == ETimeOrderType.ASC) {
			criteria.addOrder(Order.asc("startTime"));
		} else {
			criteria.addOrder(Order.desc("startTime"));
		}
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<JobHistory> listByJobId(Integer jobId, Date start, Date end) {
		Session session = sessionFacotry.getCurrentSession();
		Criteria criteria = session.createCriteria(JobHistory.class);
		criteria.add(Restrictions.eq("jobId", jobId));
		criteria.add(Restrictions.and(Restrictions.ge("startTime", start),
				Restrictions.lt("startTime", end)));
		criteria.addOrder(Order.asc("startTime"));
		return criteria.list();
	}

}