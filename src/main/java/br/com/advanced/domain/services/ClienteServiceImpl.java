package br.com.advanced.domain.services;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.advanced.domain.events.NovoClienteEvent;
import br.com.advanced.domain.exceptions.CpfJaExistenteException;
import br.com.advanced.domain.exceptions.EmailJaExistenteException;

import br.com.advanced.infrastructure.outbox.OutboxMessage;
import br.com.advanced.infrastructure.repositories.OutboxMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.advanced.domain.dtos.AlterarClienteDTO;
import br.com.advanced.domain.dtos.CriarClienteDTO;
import br.com.advanced.domain.dtos.ObterClienteDTO;
import br.com.advanced.domain.entities.Cliente;
import br.com.advanced.domain.interfaces.ClienteService;
import br.com.advanced.infrastructure.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final OutboxMessageRepository outboxMessageRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public ObterClienteDTO criar(CriarClienteDTO dto) {

        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new CpfJaExistenteException(dto.getCpf());
        }

        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaExistenteException(dto.getEmail());
        }

        var modelMapper = new ModelMapper();

        var cliente = modelMapper.map(dto, Cliente.class);

        cliente.setDataHoraCriacao(LocalDateTime.now());
        cliente.setDataHoraUltimaAlteracao(LocalDateTime.now());
        cliente.setAtivo(true);

        clienteRepository.save(cliente);

        var event = new NovoClienteEvent(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getCpf(),
                cliente.getDataHoraCriacao()
        );

        try {
            var outboxMessage = new OutboxMessage();
            outboxMessage.setAggregateType("Cliente");
            outboxMessage.setAggregateId(event.id() != null ? event.id().toString() : null);
            outboxMessage.setType("NovoCliente");
            outboxMessage.setPayload(objectMapper.writeValueAsString(event));
            outboxMessageRepository.save(outboxMessage);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }

        return modelMapper.map(cliente, ObterClienteDTO.class);
    }

    @Override
    public ObterClienteDTO alterar(AlterarClienteDTO dto) {

        var mapper = new ModelMapper();

        var cliente = clienteRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        if (verificarCpf(dto, cliente)) {
            verificarDuplicidadeCpf(dto);
            cliente.setCpf(dto.getCpf());
        }

        if (verificarEmail(dto, cliente)) {
            verificarDuplicidadeEmail(dto);
            cliente.setEmail(dto.getEmail());
        }

        if (dto.getNome() != null) cliente.setNome(dto.getNome());

        cliente.setDataHoraUltimaAlteracao(LocalDateTime.now());

        clienteRepository.save(cliente);

        return mapper.map(dto, ObterClienteDTO.class);
    }

    @Override
    public ObterClienteDTO inativar(UUID id) {

        var mapper = new ModelMapper();

        var cliente = clienteRepository.findById(id).get();

        cliente.setAtivo(false);

        clienteRepository.save(cliente);

        return mapper.map(cliente, ObterClienteDTO.class);
    }

    @Override
    public Page<ObterClienteDTO> consultarAtivos(int page, int size, String sortBy, String direction) {

        var mapper = new ModelMapper();

        var sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return clienteRepository.findByAtivoTrue(pageable)
                .map(cliente -> mapper.map(cliente, ObterClienteDTO.class));
    }

    @Override
    public ObterClienteDTO obterAtivoPorId(UUID id) {

        var mapper = new ModelMapper();

        var cliente = clienteRepository.findById(id).get();

        return mapper.map(cliente, ObterClienteDTO.class);
    }

    private boolean verificarCpf(AlterarClienteDTO dto, Cliente cliente) {
        return dto.getCpf() != null && !dto.getCpf().equals(cliente.getCpf());
    }

    private void verificarDuplicidadeCpf(AlterarClienteDTO dto) {
        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new CpfJaExistenteException(dto.getCpf());
        }
    }

    private boolean verificarEmail(AlterarClienteDTO dto, Cliente cliente) {
        return dto.getEmail() != null && !dto.getEmail().equals(cliente.getEmail());
    }

    private void verificarDuplicidadeEmail(AlterarClienteDTO dto) {
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaExistenteException(dto.getEmail());
        }
    }
}