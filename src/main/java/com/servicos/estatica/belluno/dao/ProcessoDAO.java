package com.servicos.estatica.belluno.dao;

import java.util.Calendar;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.servicos.estatica.belluno.model.Processo;
import com.servicos.estatica.belluno.util.HibernateUtil;

public class ProcessoDAO {

	public void saveProcesso(Processo processo) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(processo);
		session.getTransaction().commit();
		session.clear();
		session.close();
	}

	public void removeProcesso(Processo processo) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.remove(processo);
		session.getTransaction().commit();
		session.close();

	}

	public void updateDataInicial(Processo processo) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Processo set dhInicial = :dtIni WHERE id = :id");
		query.setParameter("dtIni", Calendar.getInstance().getTime());
		query.setParameter("id", processo.getId());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void updateDataFinal(Processo processo) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Processo set dhFinal = :dtFim WHERE id = :id");
		query.setParameter("dtFim", Calendar.getInstance().getTime());
		query.setParameter("id", processo.getId());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void updateTemperaturaMax(Processo processo) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Processo set tempMax = :tempMax WHERE id = :id");
		query.setParameter("tempMax", processo.getTempMax());
		query.setParameter("id", processo.getId());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void updateTemperaturaMin(Processo processo) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Processo set tempMin = :tempMin WHERE id = :id");
		query.setParameter("tempMin", processo.getTempMin());
		query.setParameter("id", processo.getId());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

}
