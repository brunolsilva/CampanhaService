package com.campanha.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.campanha.domain.Campanha;
import com.campanha.repository.CampanhaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CampanhaServiceTest {

	@Autowired
	private CampanhaService campanhaService;

	@Autowired
	private CampanhaRepository campanhaRepository;
	
	@Before
	public void setup() {
		campanhaRepository.deleteAll();
		List<Campanha> campanhas = Arrays.asList(
				new Campanha("Campanha 1", 1, LocalDate.of(2017, 10, 1), LocalDate.of(2017, 10, 3)),
				new Campanha("Campanha 2", 2, LocalDate.of(2017, 10, 1), LocalDate.of(2017, 10, 2))
		);
		campanhaRepository.save(campanhas);
	}

	@Test
	public void cadastroDeCampanhaAtualizandoDatasTest() {
		Campanha campanha = new Campanha("Campanha 3", 1, LocalDate.of(2017, 10, 1), LocalDate.of(2017, 10, 3));
		Campanha campanhaSalva = campanhaService.salvar(campanha);
		
		List<Campanha> campanhasAtualizadas = campanhaRepository.listarCampanhasVigentesInicioFim(LocalDate.of(2017, 10, 6));
		System.out.println(campanhasAtualizadas);
		
		assertThat(campanhasAtualizadas)
			.isNotNull()
			.isNotEmpty()
			.extracting("nome", "dataFimVigencia")
			.contains(
					tuple("Campanha 1", LocalDate.of(2017,10,5)),
					tuple("Campanha 2", LocalDate.of(2017,10,4)),
					tuple("Campanha 3", LocalDate.of(2017,10,3))
					);

		assertThat(campanhaSalva)
        	.isNotNull()
        	.extracting("nome", "time", "dataInicioVigencia", "dataFimVigencia")
        	.contains("Campanha 3", 1, LocalDate.of(2017,10,1), LocalDate.of(2017,10,3));
	}
}
