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

	private static final long serialVersionUID = 6388677275854872195L;

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
	private String primeiroFixo;
	@Column(name = "segundo_fixo")
	private String segundoFixo;
	@Column(name = "terceiro_fixo")
	private String terceiroFixo;
	@Column(name = "quarto_fixo")
	private String quartoFixo;
	@Column(name = "primeiro_sempre_aberto")
	private String primeiroSempreAberto;
	@Column(name = "primeiro_sempre_fechado")
	private String primeiroSempreFechado;
	@Column(name = "segundo_sempre_aberto")
	private String segundoSempreAberto;
	@Column(name = "segundo_sempre_fechado")
	private String segundoSempreFechado;
	@Column(name = "terceiro_sempre_aberto")
	private String terceiroSempreAberto;
	@Column(name = "terceiro_sempre_fechado")
	private String terceiroSempreFechado;
	@Column(name = "quarto_sempre_aberto")
	private String quartoSempreAberto;
	@Column(name = "quarto_sempre_fechado")
	private String quartoSempreFechado;
	@Column(name = "final_aberto")
	private String finalAberto;
	@Column(name = "final_fechado")
	private String finalFechado;
	@Column(name = "identificador")
	private String identificador;

	public CicloControle() {

	}

	public CicloControle(Long id, Integer primeiroTotal, Integer segundoTotal, Integer terceiroTotal,
			Integer quartoTotal, Integer primeiroAberto, Integer segundoAberto, Integer terceiroAberto,
			Integer quartoAberto, Integer primeiroFechado, Integer segundoFechado, Integer terceiroFechado,
			Integer quartoFechado, String primeiroFixo, String segundoFixo, String terceiroFixo, String quartoFixo,
			String primeiroSempreAberto, String primeiroSempreFechado, String segundoSempreAberto,
			String segundoSempreFechado, String terceiroSempreAberto, String terceiroSempreFechado,
			String quartoSempreAberto, String quartoSempreFechado, String finalAberto, String finalFechado,
			String identificador) {
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

	public String getPrimeiroSempreAberto() {
		return primeiroSempreAberto;
	}

	public void setPrimeiroSempreAberto(String primeiroSempreAberto) {
		this.primeiroSempreAberto = primeiroSempreAberto;
	}

	public String getPrimeiroSempreFechado() {
		return primeiroSempreFechado;
	}

	public void setPrimeiroSempreFechado(String primeiroSempreFechado) {
		this.primeiroSempreFechado = primeiroSempreFechado;
	}

	public String getSegundoSempreAberto() {
		return segundoSempreAberto;
	}

	public void setSegundoSempreAberto(String segundoSempreAberto) {
		this.segundoSempreAberto = segundoSempreAberto;
	}

	public String getSegundoSempreFechado() {
		return segundoSempreFechado;
	}

	public void setSegundoSempreFechado(String segundoSempreFechado) {
		this.segundoSempreFechado = segundoSempreFechado;
	}

	public String getTerceiroSempreAberto() {
		return terceiroSempreAberto;
	}

	public void setTerceiroSempreAberto(String terceiroSempreAberto) {
		this.terceiroSempreAberto = terceiroSempreAberto;
	}

	public String getTerceiroSempreFechado() {
		return terceiroSempreFechado;
	}

	public void setTerceiroSempreFechado(String terceiroSempreFechado) {
		this.terceiroSempreFechado = terceiroSempreFechado;
	}

	public String getQuartoSempreAberto() {
		return quartoSempreAberto;
	}

	public void setQuartoSempreAberto(String quartoSempreAberto) {
		this.quartoSempreAberto = quartoSempreAberto;
	}

	public String getQuartoSempreFechado() {
		return quartoSempreFechado;
	}

	public void setQuartoSempreFechado(String quartoSempreFechado) {
		this.quartoSempreFechado = quartoSempreFechado;
	}

	public String getFinalAberto() {
		return finalAberto;
	}

	public void setFinalAberto(String finalAberto) {
		this.finalAberto = finalAberto;
	}

	public String getFinalFechado() {
		return finalFechado;
	}

	public void setFinalFechado(String finalFechado) {
		this.finalFechado = finalFechado;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getPrimeiroFixo() {
		return primeiroFixo;
	}

	public void setPrimeiroFixo(String primeiroFixo) {
		this.primeiroFixo = primeiroFixo;
	}

	public String getSegundoFixo() {
		return segundoFixo;
	}

	public void setSegundoFixo(String segundoFixo) {
		this.segundoFixo = segundoFixo;
	}

	public String getTerceiroFixo() {
		return terceiroFixo;
	}

	public void setTerceiroFixo(String terceiroFixo) {
		this.terceiroFixo = terceiroFixo;
	}

	public String getQuartoFixo() {
		return quartoFixo;
	}

	public void setQuartoFixo(String quartoFixo) {
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
