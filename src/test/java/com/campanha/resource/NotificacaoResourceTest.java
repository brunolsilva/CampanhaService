package com.campanha.resource;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.campanha.domain.Campanha;
import com.campanha.domain.Notificacao;
import com.campanha.repository.NotificacaoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotificacaoResourceTest {
	
	@Autowired
	private NotificacaoResource notificacaoResource;
	
	@Autowired
	private NotificacaoRepository notificacaoRepository;
	
	@Before
	public void setup() {
		notificacaoRepository.deleteAll();
		List<Notificacao> notificacoes = Arrays.asList(
				new Notificacao(1, LocalDate.now()),
				new Notificacao(2, LocalDate.now())
		);

		notificacaoRepository.save(notificacoes);
	}
	
	@Test
	public void listarNotificaoTest() {
		ResponseEntity<List<Notificacao>> retorno = notificacaoResource.listar();
		assertThat(retorno).isNotNull();
		assertThat(retorno.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(retorno.getBody()).isNotEmpty();
		assertThat(retorno.getBody()).hasSize(2);
	}
}
