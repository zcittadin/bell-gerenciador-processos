package com.servicos.estatica.belluno.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import com.servicos.estatica.belluno.model.CicloControle;
import com.servicos.estatica.belluno.util.HibernateUtil;

public class CicloControleDAO {

	public void saveCicloControle(CicloControle cicloControle) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(cicloControle);
		session.getTransaction().commit();
		session.close();
	}

	public void updateCicloControle(CicloControle cicloControle) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.update(cicloControle);
		session.getTransaction().commit();
		session.close();
	}

	public void removeCicloControle(CicloControle cicloControle) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.remove(cicloControle);
		session.getTransaction().commit();
		session.close();

	}

	@SuppressWarnings("unchecked")
	public List<CicloControle> findCiclos() {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		Query query = session.createQuery("SELECT c FROM CicloControle c ORDER BY id DESC");
		query.setMaxResults(30);
		List<CicloControle> list = new ArrayList<>();
		list = query.getResultList();
		session.close();
		return list;
	}

}
