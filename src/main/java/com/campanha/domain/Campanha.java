package com.campanha.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Campanha implements Serializable {

	private static final long serialVersionUID = 868501404010911044L;

	@Id
	@GeneratedValue
	private long id;
	
	@NotEmpty(message = "Informar o nome")
	private String nome;
	
	@NotNull(message = "Informar o time")
	private Integer time;
	
	@NotNull(message = "Informar a data de Início da Campanha")
	private LocalDate dataInicioVigencia;
	
	@NotNull(message = "Informar a data de Fim da Campanha")
	private LocalDate dataFimVigencia;
	
	public Campanha() {
		/**
		 * Construtor vazio
		 */
	}

	public Campanha(String nome, Integer time, LocalDate dataInicioVigencia, LocalDate dataFimVigencia) {
		this.nome = nome;
		this.time = time;
		this.dataInicioVigencia = dataInicioVigencia;
		this.dataFimVigencia = dataFimVigencia;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public LocalDate getDataInicioVigencia() {
		return dataInicioVigencia;
	}

	public void setDataInicioVigencia(LocalDate dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	public LocalDate getDataFimVigencia() {
		return dataFimVigencia;
	}

	public void setDataFimVigencia(LocalDate dataFimVigencia) {
		this.dataFimVigencia = dataFimVigencia;
	}

	@Override
	public String toString() {
		return "Campanha [id=" + id + ", nome=" + nome + ", time=" + time + ", dataInicioVigencia=" + dataInicioVigencia
				+ ", dataFimVigencia=" + dataFimVigencia + "]";
	}

}