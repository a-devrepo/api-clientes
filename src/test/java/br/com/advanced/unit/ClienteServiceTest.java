package br.com.advanced.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import br.com.advanced.infrastructure.repositories.OutboxMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.github.javafaker.Faker;

import br.com.advanced.domain.dtos.AlterarClienteDTO;
import br.com.advanced.domain.dtos.CriarClienteDTO;
import br.com.advanced.domain.entities.Cliente;
import br.com.advanced.domain.interfaces.ClienteService;
import br.com.advanced.domain.services.ClienteServiceImpl;
import br.com.advanced.infrastructure.repositories.ClienteRepository;

public class ClienteServiceTest {

    private ClienteRepository clienteRepository;
    private ClienteService clienteService;
    private Faker faker;
    private OutboxMessageRepository outboxMessageRepository;

    @BeforeEach
    public void setUp() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        clienteRepository = mock(ClienteRepository.class);
        outboxMessageRepository =
                mock(OutboxMessageRepository.class);
        clienteService = new ClienteServiceImpl(clienteRepository, outboxMessageRepository, objectMapper);
        faker = new Faker(Locale.of("pt-BR"), new Random(100));
    }

    @Test
    @DisplayName("Deve criar um cliente com sucesso")
    public void deveCriarCLiente() throws Exception {

        var dto = getCriarClienteDTO();

        var cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setCpf(dto.getCpf());
        cliente.setAtivo(true);
        cliente.setDataHoraCriacao(LocalDateTime.now());
        cliente.setDataHoraUltimaAlteracao(LocalDateTime.now());

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        when(outboxMessageRepository
                .save(any())).thenAnswer
                (invocation -> invocation.getArgument(0));

        var response = clienteService.criar(dto);

        assertNotNull(response);
        assertEquals(response.getNome(), cliente.getNome());
        assertEquals(response.getEmail(), cliente.getEmail());
        assertEquals(response.getCpf(), cliente.getCpf());
        assertNotNull(response.getDataHoraCriacao());
        assertNotNull(response.getDataHoraUltimaAlteracao());
    }

    @Test
    @DisplayName("Deve atualizar um cliente com sucesso")
    public void deveAtualizarCLiente() {

        var id = UUID.randomUUID();

        var clienteExistente = new Cliente();
        clienteExistente.setId(UUID.randomUUID());
        clienteExistente.setNome(faker.name().fullName());
        clienteExistente.setEmail(faker.internet().emailAddress());
        clienteExistente.setCpf(faker.number().digits(11));
        clienteExistente.setAtivo(true);
        clienteExistente.setDataHoraCriacao(LocalDateTime.now());
        clienteExistente.setDataHoraUltimaAlteracao(LocalDateTime.now());

        var dto = getAlterarClienteDTO(id);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteExistente);

        var response = clienteService.alterar(dto);

        assertNotNull(response);
        assertEquals(dto.getNome(), response.getNome());
        assertEquals(dto.getEmail(), response.getEmail());
        assertEquals(dto.getCpf(), response.getCpf());
    }

    @Test
    @DisplayName("Deve inativar um cliente com sucesso")
    public void deveInativarCLiente() {

        var id = UUID.randomUUID();
        var cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(faker.name().fullName());
        cliente.setAtivo(true);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        var response = clienteService.inativar(id);

        assertNotNull(response);
        assertFalse(cliente.getAtivo());
    }

    @Test
    @DisplayName("Deve consultar clientes de forma paginada com sucesso.")
    public void deveConsultarClientes() throws Exception {

        var cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNome(faker.name().fullName());
        cliente.setEmail(faker.internet().emailAddress());
        cliente.setCpf(faker.number().digits(11));
        cliente.setAtivo(true);

        Page<Cliente> page = new PageImpl<>(List.of(cliente));
        when(clienteRepository.findByAtivoTrue(any(PageRequest.class))).thenReturn(page);

        var response = clienteService.consultarAtivos(0, 10, "nome", "asc");

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(cliente.getNome(), response.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Deve obter 1 cliente pelo ID com sucesso.")
    public void deveObterCliente() throws Exception {

        var id = UUID.randomUUID();
        var cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNome(faker.name().fullName());
        cliente.setEmail(faker.internet().emailAddress());
        cliente.setCpf(faker.number().digits(11));
        cliente.setAtivo(true);
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        var response = clienteService.obterAtivoPorId(id);

        assertNotNull(response);
        assertEquals(cliente.getId(), response.getId());
        assertEquals(cliente.getNome(), response.getNome());
        assertEquals(cliente.getEmail(), response.getEmail());
    }

    private CriarClienteDTO getCriarClienteDTO() {

        var request = new CriarClienteDTO();
        request.setNome(faker.name().fullName());
        request.setEmail(faker.internet().emailAddress());
        request.setCpf(faker.number().digits(11));

        return request;
    }

    private AlterarClienteDTO getAlterarClienteDTO(UUID id) {

        var request = new AlterarClienteDTO();
        request.setId(id);
        request.setNome(faker.name().fullName());
        request.setEmail(faker.internet().emailAddress());
        request.setCpf(faker.number().digits(11));

        return request;
    }
}
