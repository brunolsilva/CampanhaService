package com.campanha.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.campanha.domain.Campanha;
import com.campanha.exception.CampanhaNaoEncontradaException;
import com.campanha.service.CampanhaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/campanha")
public class CampanhaResource {
	
	@Autowired
	private CampanhaService campanhaService;
	
	@ApiOperation(value = "Listar campanhas", notes = "Busca as campanhas vigentes")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Campanhas retornadas com sucesso"),
		@ApiResponse(code = 204, message = "Não encontrou campanhas"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@GetMapping
	public ResponseEntity<List<Campanha>> listar() {
		List<Campanha> campanhas = campanhaService.listar();
		return !campanhas.isEmpty() ? ResponseEntity.ok(campanhas) : ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Campanha por id", notes = "Busca as últimas 50 notificações")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Campanha retornada com sucesso"),
		@ApiResponse(code = 404, message = "Campanha não encontrada"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@GetMapping("/{id}")
	public ResponseEntity<Campanha> buscaPorId(@PathVariable long id) {
		Optional<Campanha> campanha = campanhaService.buscaPorId(id);
		return campanha.isPresent() ? ResponseEntity.ok(campanha.get()) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value = "Cadastrar campanha", notes = "Cadastra a campanha")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Campanha cadastrada com sucesso"),
		@ApiResponse(code = 400, message = "Campanha enviada inválida"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@PostMapping
	public ResponseEntity<Campanha> cadastra(@Valid @RequestBody Campanha campanha) {
		Campanha campanhaSalva = campanhaService.salvar(campanha);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
				.buildAndExpand(campanhaSalva.getId()).toUri();
		return ResponseEntity.created(uri).body(campanha);
	}
	
	@ApiResponses({
		@ApiResponse(code = 200, message = "Campanha atualizada com sucesso"),
		@ApiResponse(code = 400, message = "Campanha enviada inválida"),
		@ApiResponse(code = 404, message = "Campanha não encontrada"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@ApiOperation(value = "Atualizar campanha", notes = "Atualiza a campanha")
	@PutMapping("/{id}")
	public ResponseEntity<Campanha> atualiza(
			@PathVariable long id,
			@Valid @RequestBody Campanha campanha) {
		try {
			Campanha campanhaSalva = campanhaService.atualizar(id, campanha);
			return ResponseEntity.ok(campanhaSalva);
		} catch (CampanhaNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@ApiOperation(value = "Remover campanha", notes = "Remove a campanha")
	@ApiResponses({
		@ApiResponse(code = 204, message = "Campanha removida com sucesso"),
		@ApiResponse(code = 400, message = "Campanha enviada inválida"),
		@ApiResponse(code = 404, message = "Campanha não encontrada"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remove(@PathVariable long id) {
		campanhaService.remove(id);
		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Buscar campanhas por time", notes = "Busca as campanhas vigentes do time")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Campanhas encontradas"),
		@ApiResponse(code = 204, message = "Campanhas não encontradas"),
		@ApiResponse(code = 400, message = "Time enviado inválido"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@GetMapping("/time/{time}")
	public ResponseEntity<List<Campanha>> getCampanhasPorTime(@PathVariable Integer time) {
		List<Campanha> campanhas = campanhaService.listarPorTime(time);
		return !campanhas.isEmpty() ? ResponseEntity.ok(campanhas) : ResponseEntity.noContent().build();
	}
}