package com.servicos.estatica.belluno.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "processo")
public class Processo implements Serializable {

	private static final long serialVersionUID = 689884747660859207L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@OneToMany(mappedBy = "processo", targetEntity = Leitura.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Leitura> leituras;
	@Column(name = "identificador")
	private String identificador;
	@Column(name = "t_max")
	private Integer tempMax;
	@Column(name = "t_min")
	private Integer tempMin;
	@Column(name = "dh_inicial")
	private Date dhInicial;
	@Column(name = "dh_final")
	private Date dhFinal;

	public Processo() {

	}

	public Processo(Long id, List<Leitura> leituras, String identificador, Integer tempMax, Integer tempMin,
			Date dhInicial, Date dhFinal) {
		this.id = id;
		this.leituras = leituras;
		this.identificador = identificador;
		this.tempMax = tempMax;
		this.tempMin = tempMin;
		this.dhInicial = dhInicial;
		this.dhFinal = dhFinal;
	}

	public Date getDhInicial() {
		return dhInicial;
	}

	public void setDhInicial(Date dhInicial) {
		this.dhInicial = dhInicial;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Leitura> getLeituras() {
		return leituras;
	}

	public void setLeituras(List<Leitura> leituras) {
		this.leituras = leituras;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Integer getTempMax() {
		return tempMax;
	}

	public void setTempMax(Integer tempMax) {
		this.tempMax = tempMax;
	}

	public Integer getTempMin() {
		return tempMin;
	}

	public void setTempMin(Integer tempMin) {
		this.tempMin = tempMin;
	}

	public Date getDhFinal() {
		return dhFinal;
	}

	public void setDhFinal(Date dhFinal) {
		this.dhFinal = dhFinal;
	}

	@Override
	public String toString() {
		return "Processo [id=" + id + ", leituras=" + leituras + ", identificador=" + identificador + ", tempMax="
				+ tempMax + ", tempMin=" + tempMin + ", dhInicial=" + dhInicial + ", dhFinal=" + dhFinal + "]";
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
