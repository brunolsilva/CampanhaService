package com.campanha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campanha.domain.Notificacao;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

	public List<Notificacao> findTop50ByOrderByDataAtualizacaoDesc();
}
