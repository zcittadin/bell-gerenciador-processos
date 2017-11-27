package com.servicos.estatica.belluno.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "controle")
public class CicloControle implements Serializable {

	private static final long serialVersionUID = 2996529224210643219L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "primeiro_total")
	private Integer primeiroTotal;
	@Column(name = "segundo_total")
	private Integer segundoTotal;
	@Column(name = "terceiro_total")
	private Integer terceiroTotal;
	@Column(name = "quarto_total")
	private Integer quartoTotal;
	@Column(name = "primeiro_aberto")
	private Integer primeiroAberto;
	@Column(name = "segundo_aberto")
	private Integer segundoAberto;
	@Column(name = "terceiro_aberto")
	private Integer terceiroAberto;
	@Column(name = "quarto_aberto")
	private Integer quartoAberto;
	@Column(name = "primeiro_fechado")
	private Integer primeiroFechado;
	@Column(name = "segundo_fechado")
	private Integer segundoFechado;
	@Column(name = "terceiro_fechado")
	private Integer terceiroFechado;
	@Column(name = "quarto_fechado")
	private Integer quartoFechado;
	@Column(name = "primeiro_fixo")
	private char primeiroFixo;
	@Column(name = "segundo_fixo")
	private char segundoFixo;
	@Column(name = "terceiro_fixo")
	private char terceiroFixo;
	@Column(name = "quarto_fixo")
	private char quartoFixo;
	@Column(name = "primeiro_sempre_aberto")
	private char primeiroSempreAberto;
	@Column(name = "primeiro_sempre_fechado")
	private char primeiroSempreFechado;
	@Column(name = "segundo_sempre_aberto")
	private char segundoSempreAberto;
	@Column(name = "segundo_sempre_fechado")
	private char segundoSempreFechado;
	@Column(name = "terceiro_sempre_aberto")
	private char terceiroSempreAberto;
	@Column(name = "terceiro_sempre_fechado")
	private char terceiroSempreFechado;
	@Column(name = "quarto_sempre_aberto")
	private char quartoSempreAberto;
	@Column(name = "quarto_sempre_fechado")
	private char quartoSempreFechado;
	@Column(name = "final_aberto")
	private char finalAberto;
	@Column(name = "final_fechado")
	private char finalFechado;
	@Column(name = "identificador")
	private String identificador;

	public CicloControle() {

	}

	public CicloControle(Long id, Integer primeiroTotal, Integer segundoTotal, Integer terceiroTotal,
			Integer quartoTotal, Integer primeiroAberto, Integer segundoAberto, Integer terceiroAberto,
			Integer quartoAberto, Integer primeiroFechado, Integer segundoFechado, Integer terceiroFechado,
			Integer quartoFechado, char primeiroFixo, char segundoFixo, char terceiroFixo, char quartoFixo,
			char primeiroSempreAberto, char primeiroSempreFechado, char segundoSempreAberto, char segundoSempreFechado,
			char terceiroSempreAberto, char terceiroSempreFechado, char quartoSempreAberto, char quartoSempreFechado,
			char finalAberto, char finalFechado, String identificador) {
		this.id = id;
		this.primeiroTotal = primeiroTotal;
		this.segundoTotal = segundoTotal;
		this.terceiroTotal = terceiroTotal;
		this.quartoTotal = quartoTotal;
		this.primeiroAberto = primeiroAberto;
		this.segundoAberto = segundoAberto;
		this.terceiroAberto = terceiroAberto;
		this.quartoAberto = quartoAberto;
		this.primeiroFechado = primeiroFechado;
		this.segundoFechado = segundoFechado;
		this.terceiroFechado = terceiroFechado;
		this.quartoFechado = quartoFechado;
		this.primeiroFixo = primeiroFixo;
		this.segundoFixo = segundoFixo;
		this.terceiroFixo = terceiroFixo;
		this.quartoFixo = quartoFixo;
		this.primeiroSempreAberto = primeiroSempreAberto;
		this.primeiroSempreFechado = primeiroSempreFechado;
		this.segundoSempreAberto = segundoSempreAberto;
		this.segundoSempreFechado = segundoSempreFechado;
		this.terceiroSempreAberto = terceiroSempreAberto;
		this.terceiroSempreFechado = terceiroSempreFechado;
		this.quartoSempreAberto = quartoSempreAberto;
		this.quartoSempreFechado = quartoSempreFechado;
		this.finalAberto = finalAberto;
		this.finalFechado = finalFechado;
		this.identificador = identificador;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPrimeiroTotal() {
		return primeiroTotal;
	}

	public void setPrimeiroTotal(Integer primeiroTotal) {
		this.primeiroTotal = primeiroTotal;
	}

	public Integer getSegundoTotal() {
		return segundoTotal;
	}

	public void setSegundoTotal(Integer segundoTotal) {
		this.segundoTotal = segundoTotal;
	}

	public Integer getTerceiroTotal() {
		return terceiroTotal;
	}

	public void setTerceiroTotal(Integer terceiroTotal) {
		this.terceiroTotal = terceiroTotal;
	}

	public Integer getQuartoTotal() {
		return quartoTotal;
	}

	public void setQuartoTotal(Integer quartoTotal) {
		this.quartoTotal = quartoTotal;
	}

	public Integer getPrimeiroAberto() {
		return primeiroAberto;
	}

	public void setPrimeiroAberto(Integer primeiroAberto) {
		this.primeiroAberto = primeiroAberto;
	}

	public Integer getSegundoAberto() {
		return segundoAberto;
	}

	public void setSegundoAberto(Integer segundoAberto) {
		this.segundoAberto = segundoAberto;
	}

	public Integer getTerceiroAberto() {
		return terceiroAberto;
	}

	public void setTerceiroAberto(Integer terceiroAberto) {
		this.terceiroAberto = terceiroAberto;
	}

	public Integer getQuartoAberto() {
		return quartoAberto;
	}

	public void setQuartoAberto(Integer quartoAberto) {
		this.quartoAberto = quartoAberto;
	}

	public Integer getPrimeiroFechado() {
		return primeiroFechado;
	}

	public void setPrimeiroFechado(Integer primeiroFechado) {
		this.primeiroFechado = primeiroFechado;
	}

	public Integer getSegundoFechado() {
		return segundoFechado;
	}

	public void setSegundoFechado(Integer segundoFechado) {
		this.segundoFechado = segundoFechado;
	}

	public Integer getTerceiroFechado() {
		return terceiroFechado;
	}

	public void setTerceiroFechado(Integer terceiroFechado) {
		this.terceiroFechado = terceiroFechado;
	}

	public Integer getQuartoFechado() {
		return quartoFechado;
	}

	public void setQuartoFechado(Integer quartoFechado) {
		this.quartoFechado = quartoFechado;
	}

	public char getPrimeiroSempreAberto() {
		return primeiroSempreAberto;
	}

	public void setPrimeiroSempreAberto(char primeiroSempreAberto) {
		this.primeiroSempreAberto = primeiroSempreAberto;
	}

	public char getPrimeiroSempreFechado() {
		return primeiroSempreFechado;
	}

	public void setPrimeiroSempreFechado(char primeiroSempreFechado) {
		this.primeiroSempreFechado = primeiroSempreFechado;
	}

	public char getSegundoSempreAberto() {
		return segundoSempreAberto;
	}

	public void setSegundoSempreAberto(char segundoSempreAberto) {
		this.segundoSempreAberto = segundoSempreAberto;
	}

	public char getSegundoSempreFechado() {
		return segundoSempreFechado;
	}

	public void setSegundoSempreFechado(char segundoSempreFechado) {
		this.segundoSempreFechado = segundoSempreFechado;
	}

	public char getTerceiroSempreAberto() {
		return terceiroSempreAberto;
	}

	public void setTerceiroSempreAberto(char terceiroSempreAberto) {
		this.terceiroSempreAberto = terceiroSempreAberto;
	}

	public char getTerceiroSempreFechado() {
		return terceiroSempreFechado;
	}

	public void setTerceiroSempreFechado(char terceiroSempreFechado) {
		this.terceiroSempreFechado = terceiroSempreFechado;
	}

	public char getQuartoSempreAberto() {
		return quartoSempreAberto;
	}

	public void setQuartoSempreAberto(char quartoSempreAberto) {
		this.quartoSempreAberto = quartoSempreAberto;
	}

	public char getQuartoSempreFechado() {
		return quartoSempreFechado;
	}

	public void setQuartoSempreFechado(char quartoSempreFechado) {
		this.quartoSempreFechado = quartoSempreFechado;
	}

	public char getFinalAberto() {
		return finalAberto;
	}

	public void setFinalAberto(char finalAberto) {
		this.finalAberto = finalAberto;
	}

	public char getFinalFechado() {
		return finalFechado;
	}

	public void setFinalFechado(char finalFechado) {
		this.finalFechado = finalFechado;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public char getPrimeiroFixo() {
		return primeiroFixo;
	}

	public void setPrimeiroFixo(char primeiroFixo) {
		this.primeiroFixo = primeiroFixo;
	}

	public char getSegundoFixo() {
		return segundoFixo;
	}

	public void setSegundoFixo(char segundoFixo) {
		this.segundoFixo = segundoFixo;
	}

	public char getTerceiroFixo() {
		return terceiroFixo;
	}

	public void setTerceiroFixo(char terceiroFixo) {
		this.terceiroFixo = terceiroFixo;
	}

	public char getQuartoFixo() {
		return quartoFixo;
	}

	public void setQuartoFixo(char quartoFixo) {
		this.quartoFixo = quartoFixo;
	}

	@Override
	public String toString() {
		return identificador;
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
		CicloControle other = (CicloControle) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
