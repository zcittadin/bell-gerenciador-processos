package com.servicos.estatica.belluno.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import com.servicos.estatica.belluno.model.Leitura;
import com.servicos.estatica.belluno.model.Processo;
import com.servicos.estatica.belluno.util.HibernateUtil;

public class LeituraDAO {

	public void saveLeitura(Leitura leitura) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(leitura);
		session.getTransaction().commit();
		session.close();

	}

	@SuppressWarnings("unchecked")
	public void removeLeituras(Processo processo) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hql = "SELECT l FROM Leitura l WHERE l.processo = " + processo.getId();
		Query query = session.createQuery(hql);
		List<Leitura> list = query.getResultList();
		if (list.isEmpty()) {
			session.close();
			return;
		}
		for (Leitura leitura : list) {
			session.remove(leitura);
		}
		session.getTransaction().commit();
		session.close();
	}

}
