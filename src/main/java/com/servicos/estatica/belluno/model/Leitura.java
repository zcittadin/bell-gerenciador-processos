package com.servicos.estatica.belluno.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "leituras")
public class Leitura implements Serializable {

	private static final long serialVersionUID = -4305566712675037027L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_processo")
	private Processo processo;
	@Column(name = "dt_proc")
	private Date dtProc;
	@Column(name = "temp")
	private double temp;
	@Column(name = "sp")
	private double sp;

	public Leitura() {

	}

	public Leitura(Long id, Processo processo, Date dtProc, double temp, double sp) {
		this.id = id;
		this.processo = processo;
		this.dtProc = dtProc;
		this.temp = temp;
		this.sp = sp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public Date getDtProc() {
		return dtProc;
	}

	public void setDtProc(Date dtProc) {
		this.dtProc = dtProc;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getSp() {
		return sp;
	}

	public void setSp(double sp) {
		this.sp = sp;
	}

	@Override
	public String toString() {
		return "Leitura [id=" + id + ", processo=" + processo + ", dtProc=" + dtProc + ", temp=" + temp + ", sp=" + sp
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Leitura other = (Leitura) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
