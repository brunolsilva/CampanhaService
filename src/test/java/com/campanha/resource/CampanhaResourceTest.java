package com.campanha.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.campanha.domain.Campanha;
import com.campanha.repository.CampanhaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CampanhaResourceTest {

	@Autowired
	private CampanhaResource campanhaResource;

	@Autowired
	private CampanhaRepository campanhaRepository;
	
	@Before
	public void setup() {
		campanhaRepository.deleteAll();
		List<Campanha> campanhas = Arrays.asList(
				new Campanha("black friday", 1, LocalDate.now(), LocalDate.now().plusDays(5)),
				new Campanha("natal", 2, LocalDate.now(), LocalDate.now().plusDays(7))
		);
		campanhaRepository.save(campanhas);
	}

	@Test
	public void semCampanhasTest() {
		campanhaRepository.deleteAll();
		ResponseEntity<List<Campanha>> retorno = campanhaResource.listar();
		assertThat(retorno).isNotNull();
		assertThat(retorno.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(retorno.getBody()).isNull();
	}

	@Test
	public void semCampanhasAtivasTest() {
		campanhaRepository.findAll().forEach(c -> {
			c.setDataInicioVigencia(LocalDate.now().minusDays(30));
			c.setDataFimVigencia(LocalDate.now().minusDays(10));
			campanhaRepository.save(c);
		});
		ResponseEntity<List<Campanha>> retorno = campanhaResource.listar();
		assertThat(retorno).isNotNull();
		assertThat(retorno.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(retorno.getBody()).isNull();
	}

	@Test
	public void retornaCampanhasAtivasTest() {
		ResponseEntity<List<Campanha>> retorno = campanhaResource.listar();
		assertThat(retorno).isNotNull();
		assertThat(retorno.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(retorno.getBody()).isNotEmpty();
		assertThat(retorno.getBody()).hasSize(2);
	}

	@Test
	public void buscarCampanhaPorIdTest() {
		Optional<Campanha> campanha = campanhaRepository.listarCampanhasVigentes(LocalDate.now()).stream().findAny();
		assertThat(campanha.isPresent()).isTrue();
		ResponseEntity<Campanha> retorno = campanhaResource.buscaPorId(campanha.get().getId());
		assertThat(retorno).isNotNull();
		assertThat(retorno.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(retorno.getBody().getId()).isEqualTo(campanha.get().getId());
	}

	@Test
	public void cadastrarCampanhaTest() {
		Campanha campanha = new Campanha("teste", 1, LocalDate.now(), LocalDate.now().plusDays(30));
		ResponseEntity<Campanha> retorno = campanhaResource.cadastra(campanha);
		assertThat(retorno).isNotNull();
		assertThat(retorno.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(retorno.getHeaders().get("location")).isNotNull();
		assertThat(retorno.getBody()).isNotNull();
		assertThat(retorno.getBody().getId()).isNotNull();
	}

	@Test
	public void cadastrarCampanhaInvalidaTest() {
		Campanha campanha = new Campanha("", 1, LocalDate.now(), LocalDate.now().plusDays(30));
		assertThatThrownBy(() -> {campanhaResource.cadastra(campanha);}).isInstanceOf(ConstraintViolationException.class);
	}

	@Test	
	public void atualizarCampanhaTest() {
		Optional<Campanha> campanha = campanhaRepository.listarCampanhasVigentes(LocalDate.now()).stream().findAny();
		assertThat(campanha.isPresent()).isTrue();
		String novoNome = "Novo nome";
		campanha.get().setNome(novoNome);
		ResponseEntity<Campanha> retorno = campanhaResource.atualiza(campanha.get().getId(), campanha.get());
		assertThat(retorno).isNotNull();
		assertThat(retorno.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(retorno.getBody()).isNotNull();
		assertThat(retorno.getBody().getNome()).isEqualTo(novoNome);
		
	}

	@Test	
	public void deletarCampanhaTest() {
		Optional<Campanha> campanha = campanhaRepository.listarCampanhasVigentes(LocalDate.now()).stream().findAny();
		assertThat(campanha.isPresent()).isTrue();
		ResponseEntity<?> retorno = campanhaResource.remove(campanha.get().getId());
		assertThat(retorno).isNotNull();
		assertThat(retorno.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Optional<Campanha> campanhaDeletada = campanhaRepository.findCampanhaVigente(campanha.get().getId(), LocalDate.now());
		assertThat(campanhaDeletada.isPresent()).isFalse();
		
	}

	@Test	
	public void listarPorTimeTest() {
		ResponseEntity<List<Campanha>> retorno = campanhaResource.getCampanhasPorTime(1);
		assertThat(retorno).isNotNull();
		assertThat(retorno.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(retorno.getBody()).isNotEmpty();
		assertThat(retorno.getBody()).hasSize(1);
	}
}
