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

	private static final long serialVersionUID = -5827376150440761796L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@OneToMany(mappedBy = "processo", targetEntity = Leitura.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Leitura> leituras;
	@Column(name = "identificador")
	private String identificador;
	@Column(name = "t_max")
	private double tempMax;
	@Column(name = "t_min")
	private double tempMin;
	@Column(name = "dh_inicial")
	private Date dhInicial;
	@Column(name = "duracao")
	private Date duracao;

	public Processo() {

	}

	public Processo(Long id, List<Leitura> leituras, String identificador, double tempMax, double tempMin,
			Date dhInicial, Date duracao) {
		this.id = id;
		this.leituras = leituras;
		this.identificador = identificador;
		this.tempMax = tempMax;
		this.tempMin = tempMin;
		this.dhInicial = dhInicial;
		this.duracao = duracao;
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

	public double getTempMax() {
		return tempMax;
	}

	public void setTempMax(double tempMax) {
		this.tempMax = tempMax;
	}

	public double getTempMin() {
		return tempMin;
	}

	public void setTempMin(double tempMin) {
		this.tempMin = tempMin;
	}

	public Date getDuracao() {
		return duracao;
	}

	public void setDuracao(Date duracao) {
		this.duracao = duracao;
	}

	@Override
	public String toString() {
		return "Processo [id=" + id + ", leituras=" + leituras + ", identificador=" + identificador + ", tempMax="
				+ tempMax + ", tempMin=" + tempMin + ", dhInicial=" + dhInicial + ", duracao=" + duracao + "]";
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
