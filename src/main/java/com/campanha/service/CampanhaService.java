package com.campanha.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.campanha.domain.Campanha;
import com.campanha.domain.Notificacao;
import com.campanha.exception.CampanhaNaoEncontradaException;
import com.campanha.repository.CampanhaRepository;
import com.campanha.repository.NotificacaoRepository;

/**
 * Serviço responsável pelo gerenciamento de Campanhas
 *
 */
@Transactional
@Service
public class CampanhaService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CampanhaService.class);
	
	@Autowired
	private CampanhaRepository campanhaRepository;
	
	@Autowired
	private NotificacaoRepository notificacaoRepository;
	
	/**
	 * Busca as campanhas vigentes no momento
	 * 
	 * @return List<Campanha> a lista com as campanhas vigentes
	 */
	public List<Campanha> listar() {
		return campanhaRepository.listarCampanhasVigentes(LocalDate.now());
	}
	
	/**
	 * Busca uma campanha vigente com o id passado
	 * 
	 * @param id O id da campanha
	 * @return Optional com a campanha com o id passado se esta existir e estiver vigente
	 */
	public Optional<Campanha> buscaPorId(long id) {
		return campanhaRepository.findCampanhaVigente(id, LocalDate.now());
	}
	
	/**
	 * Cria a campanha passada e atualiza as campanhas na mesma vigência
	 * 
	 * @param campanha A Campanha a ser salva
	 * @return A Campanha salva com seu id
	 */
	public Campanha salvar(Campanha campanha) {
		atualizaDataCampanhasExistentes(campanha);
		return campanhaRepository.save(campanha);
	}

	/**
	 * Atualiza a campanha passada e cria uma notificação de que esta foi atualizada
	 * 
	 * @param id O id da campanha
	 * @param campanha A Campanha a ser atualizada
	 * @return A Campanha atualizada
	 */
	public Campanha atualizar(long id, Campanha campanha) {
		Optional<Campanha> campanhaExistente = buscaPorId(id);
		if (!campanhaExistente.isPresent()) {
			throw new CampanhaNaoEncontradaException();
		}
		BeanUtils.copyProperties(campanha, campanhaExistente.get(), "id");
		campanha = campanhaRepository.save(campanhaExistente.get());
		try {
			notificacaoRepository.save(new Notificacao(campanha.getId(), LocalDate.now()));
		} catch (Exception e) {
			LOGGER.error("Erro ao notificar atualização da campanha {}", id);
		}

		return campanha;
	}
	
	public void remove(long id) {
		campanhaRepository.delete(id);
	}
	
	/**
	 * Retorna as campanhas vigentes do time
	 * 
	 * @param time O id do time
	 * @return List<Campanha> as campanhas vigentes do time
	 */
	public List<Campanha> listarPorTime(Integer time) {
		return campanhaRepository.listarCampanhasVigentes(LocalDate.now(), time);
	}
	
	/**
	 * Atualiza as campanhas que estão vigentes no mesmo período que a Campanha criada <br>
	 * e cria uma notificação para cada campanha atualizada
	 * @param campanha A campanha sendo cadastrada
	 */
	private void atualizaDataCampanhasExistentes(Campanha campanha) {
		List<Campanha> campanhasAtivas = campanhaRepository.listarCampanhasVigentesInicioFim(campanha.getDataFimVigencia());
		System.out.println("ativas " + campanhasAtivas);
		campanhasAtivas.stream().forEach(c -> {
			c.setDataFimVigencia(c.getDataFimVigencia().plusDays(1));
			while (campanhasAtivas.stream()
					.filter(cc -> 
							(cc.getId() != c.getId()) || (cc.getId() == c.getId() && campanhasAtivas.size() == 1))
					.filter(cc2 ->
							(cc2.getDataFimVigencia().isEqual(c.getDataFimVigencia()) && campanhasAtivas.size() != 1)
							|| c.getDataFimVigencia().isEqual(campanha.getDataFimVigencia()))
					.count() != 0) 
			{
				c.setDataFimVigencia(c.getDataFimVigencia().plusDays(1));
			}
		});
		campanhaRepository.save(campanhasAtivas);

		List<Notificacao> notificacoes = new ArrayList<>();
		campanhasAtivas.forEach(campanhaAtualizada -> {
			notificacoes.add(new Notificacao(campanhaAtualizada.getId(), LocalDate.now()));
		});

		try {
			notificacaoRepository.save(notificacoes);
		} catch (Exception e) {
			LOGGER.error("Erro ao notificar atualização das campanhas {}", notificacoes.stream().map(Notificacao::getCampanha).collect(Collectors.toList()));
		}
	}
}