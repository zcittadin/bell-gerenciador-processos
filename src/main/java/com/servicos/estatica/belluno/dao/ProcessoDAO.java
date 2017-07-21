package com.servicos.estatica.belluno.dao;

import org.hibernate.Session;

import com.servicos.estatica.belluno.model.Processo;
import com.servicos.estatica.belluno.util.HibernateUtil;

public class ProcessoDAO {

	public void saveProcesso(Processo processo) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(processo);
		session.getTransaction().commit();
		session.close();

	}

}
