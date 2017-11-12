package com.campanha.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campanha.domain.Notificacao;
import com.campanha.repository.NotificacaoRepository;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/notificacao")
public class NotificacaoResource {

	@Autowired
	private NotificacaoRepository notificacaoRepository;
	
	@ApiOperation(value = "Busca as últimas 50 notificações", notes = "Busca as últimas 50 notificações")
	@GetMapping
	public ResponseEntity<List<Notificacao>> listar() {
		List<Notificacao> notificacoes = notificacaoRepository.findTop50ByOrderByDataAtualizacaoDesc();
		return notificacoes != null && !notificacoes.isEmpty() ? ResponseEntity.ok(notificacoes) : ResponseEntity.noContent().build();
	}
}
