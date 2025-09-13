package br.com.advanced.domain.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;

import br.com.advanced.domain.dtos.AlterarClienteDTO;
import br.com.advanced.domain.dtos.CriarClienteDTO;
import br.com.advanced.domain.dtos.ObterClienteDTO;

public interface ClienteService {

	ObterClienteDTO criar(CriarClienteDTO dto);
	
	ObterClienteDTO alterar(AlterarClienteDTO dto);
	
	ObterClienteDTO inativar(UUID id);
	
	Page<ObterClienteDTO> consultarAtivos(int page, int size, String sortBy, String direction);

	ObterClienteDTO obterAtivoPorId(UUID id);
}
