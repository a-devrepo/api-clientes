package br.com.advanced.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.advanced.domain.dtos.AlterarClienteDTO;
import br.com.advanced.domain.dtos.CriarClienteDTO;
import br.com.advanced.domain.dtos.ObterClienteDTO;
import br.com.advanced.domain.interfaces.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

	private final ClienteService clienteService;
	
	@PostMapping
	public ResponseEntity<ObterClienteDTO> post(@RequestBody @Valid CriarClienteDTO dto) {
		var response = clienteService.criar(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PatchMapping
	public ResponseEntity<ObterClienteDTO> patch(@RequestBody @Valid AlterarClienteDTO dto) {
		var response = clienteService.alterar(dto);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ObterClienteDTO> delete(@PathVariable UUID id) {
		var response = clienteService.inativar(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping
	public ResponseEntity<Page<ObterClienteDTO>> getAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int size,
			@RequestParam(defaultValue = "nome") String sortBy,
			@RequestParam(defaultValue = "asc") String direction
			) {
		var response = clienteService.consultarAtivos(page, size, sortBy, direction);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ObterClienteDTO> getById(@PathVariable UUID id) {
		var response = clienteService.obterAtivoPorId(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}