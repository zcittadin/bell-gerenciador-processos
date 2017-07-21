package com.servicos.estatica.belluno.dao;

import org.hibernate.Session;

import com.servicos.estatica.belluno.model.Leitura;
import com.servicos.estatica.belluno.util.HibernateUtil;

public class LeituraDAO {

	public void saveLeitura(Leitura leitura) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(leitura);
		session.getTransaction().commit();
		session.close();

	}

}
