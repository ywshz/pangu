package org.yws.pangu.dao.mysql;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yws.pangu.domain.JobBean;
import org.yws.pangu.enums.EJobRunType;

import java.util.List;

@Repository
public class MySqlJobDao {
    @Autowired
    private SessionFactory sessionFacotry;

    @SuppressWarnings("unchecked")
    public List<JobBean> list(Integer groupId) {
        Session session = sessionFacotry.getCurrentSession();

        Criteria criteria = session.createCriteria(JobBean.class);
        criteria.add(Restrictions.eq("groupId", groupId));

        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<JobBean> listAll() {
        Session session = sessionFacotry.getCurrentSession();
        Criteria criteria = session.createCriteria(JobBean.class);
        criteria.addOrder(Order.desc("gmtCreate"));
        return criteria.list();
    }


    @SuppressWarnings("unchecked")
    public List<JobBean> getAutoRunJobs() {
        Session session = sessionFacotry.getCurrentSession();

        Criteria criteria = session.createCriteria(JobBean.class);
        criteria.add(Restrictions.eq("auto", EJobRunType.AUTO.ordinal()));

        return criteria.list();
    }

    public JobBean getJob(Integer id) {
        Session session = sessionFacotry.getCurrentSession();

        return (JobBean) session.get(JobBean.class, id);
    }

    public void update(JobBean job) {
        Session session = sessionFacotry.getCurrentSession();
        session.update(job);
        session.flush();
    }

    public Integer save(JobBean job) {
        Session session = sessionFacotry.getCurrentSession();
        session.save(job);
        session.flush();
        return job.getId();
    }

    public void delete(JobBean job) {
        Session session = sessionFacotry.getCurrentSession();
        session.delete(job);
        session.flush();
    }

    public List<JobBean> findByScript(String key) {
        Session session = sessionFacotry.getCurrentSession();

        Criteria criteria = session.createCriteria(JobBean.class);
        criteria.add(Restrictions.like("script", key, MatchMode.ANYWHERE));

        criteria.setMaxResults(100);
        return criteria.list();
    }
}