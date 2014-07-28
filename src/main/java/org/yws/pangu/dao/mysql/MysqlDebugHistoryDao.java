package org.yws.pangu.dao.mysql;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yws.pangu.domain.DebugHistory;

import java.util.List;

/**
 * Created by wangshu.yang on 2014/7/28.
 */
@Repository
public class MysqlDebugHistoryDao {
    @Autowired
    private SessionFactory sessionFacotry;

    public DebugHistory get(Long id){
        Session session = sessionFacotry.getCurrentSession();
        return (DebugHistory)session.get(DebugHistory.class, id);
    }

    public void update(DebugHistory his) {
        Session session = sessionFacotry.getCurrentSession();
        session.update(his);
        session.flush();
    }

    public List<DebugHistory> list(Integer fileId) {
        Session session = sessionFacotry.getCurrentSession();
        Criteria criteria = session.createCriteria(DebugHistory.class);
        criteria.add(Restrictions.eq("fileId",fileId));
        criteria.addOrder(Order.desc("startTime"));
        criteria.setMaxResults(20);
        return criteria.list();
    }

    public void save(DebugHistory his) {
        Session session = sessionFacotry.getCurrentSession();
        session.save(his);
        session.flush();
    }
}
