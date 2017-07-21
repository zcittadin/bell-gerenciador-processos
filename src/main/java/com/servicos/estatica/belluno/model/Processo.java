package com.servicos.estatica.belluno.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "processo")
public class Processo implements Serializable {

	private static final long serialVersionUID = -3589796632173698057L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "identificador")
	private String identificador;
	@Column(name = "dt_proc")
	private Date dtProc;
	@Column(name = "temp")
	private double temp;
	@Column(name = "sp")
	private double sp;

	public Processo() {

	}

	public Processo(Long id, String identificador, Date dtProc, double temp, double sp) {
		super();
		this.id = id;
		this.identificador = identificador;
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

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
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
		return "Processo [id=" + id + ", identificador=" + identificador + ", dtProc=" + dtProc + ", temp=" + temp
				+ ", sp=" + sp + "]";
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
		Processo other = (Processo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
