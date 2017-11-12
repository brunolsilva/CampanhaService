package com.campanha.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@JsonInclude(value = Include.NON_NULL)
public class Notificacao {

	@Id
	@GeneratedValue
	private long id;
	private long campanha;
	private LocalDate dataAtualizacao;
	
	public Notificacao() {
		/**
		 * Construtor vazio
		 */
	}
	
	public Notificacao(long campanha, LocalDate dataAtualizacao) {
		this.campanha = campanha;
		this.dataAtualizacao = dataAtualizacao;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCampanha() {
		return campanha;
	}

	public void setCampanha(long campanha) {
		this.campanha = campanha;
	}

	public LocalDate getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(LocalDate dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@Override
	public String toString() {
		return "Notificacao [id=" + id + ", campanha=" + campanha + ", dataAtualizacao=" + dataAtualizacao + "]";
	}
}
