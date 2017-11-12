package com.campanha.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.campanha.domain.Campanha;

public interface CampanhaRepository extends JpaRepository<Campanha, Long> {
	
	@Query("select c from Campanha c where c.dataInicioVigencia <= ?1 and c.dataFimVigencia >= ?1 order by c.dataFimVigencia")
	public List<Campanha> listarCampanhasVigentes(LocalDate data);

	@Query("select c from Campanha c where c.dataInicioVigencia <= ?2 and c.dataFimVigencia >= ?2 and c.id = ?1")
	public Optional<Campanha> findCampanhaVigente(long id, LocalDate data);
	
	@Query("select c from Campanha c where ?1 >= c.dataInicioVigencia or ?1 >= c.dataFimVigencia order by c.dataFimVigencia")
	public List<Campanha> listarCampanhasVigentesInicioFim(LocalDate dataFimVigencia);

	@Query("select c from Campanha c where c.dataInicioVigencia <= ?1 and c.dataFimVigencia >= ?1 and time = ?2 order by c.dataFimVigencia")
	public List<Campanha> listarCampanhasVigentes(LocalDate data, Integer time);
}
