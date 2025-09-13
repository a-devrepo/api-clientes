package br.com.advanced.domain.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.advanced.domain.dtos.AlterarClienteDTO;
import br.com.advanced.domain.dtos.CriarClienteDTO;
import br.com.advanced.domain.dtos.ObterClienteDTO;
import br.com.advanced.domain.interfaces.ClienteService;
import br.com.advanced.infrastructure.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository repository;
	
	@Override
	public ObterClienteDTO criar(CriarClienteDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObterClienteDTO alterar(AlterarClienteDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObterClienteDTO inativar(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<ObterClienteDTO> consultar(int page, int size, String sortBy, String direction) {
		// TODO Auto-generated method stub
		return null;
	}
}