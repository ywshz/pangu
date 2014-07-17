package org.yws.pangu.dao.mysql;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yws.pangu.domain.FileBean;
import org.yws.pangu.domain.FileDescriptor;

@Repository
public class MySqlFileDao {
	@Autowired
	private SessionFactory sessionFacotry;

	public FileBean fillFile(FileDescriptor parent) {
		FileBean fb = new FileBean(parent);
		Session session = sessionFacotry.getCurrentSession();

		if (parent == null) {
			return null;
		}

		Criteria c = session.createCriteria(FileDescriptor.class);
		c.add(Restrictions.eq("owner", parent.getOwner()));
		c.add(Restrictions.eq("parent", parent.getId()));
		List<FileDescriptor> list = c.list();
		fb.setSubFiles(list);

		return fb;
	}

	public FileDescriptor getFile(Integer id, String owner) {
		Session session = sessionFacotry.getCurrentSession();
		FileDescriptor fd = null;
		if (id == null) {
			Criteria c = session.createCriteria(FileDescriptor.class);
			c.add(Restrictions.eq("owner", owner));
			c.add(Restrictions.isNull("parent"));

			fd = (FileDescriptor) c.uniqueResult();
		} else {
			fd = (FileDescriptor) session.get(FileDescriptor.class, id);
		}

		return fd;
	}

	public void update(FileDescriptor fd) {
		Session session = sessionFacotry.getCurrentSession();
		session.update(fd);
	}

	public Integer save(FileDescriptor fd) {
		Session session = sessionFacotry.getCurrentSession();
		session.save(fd);
		return fd.getId();
	}

}