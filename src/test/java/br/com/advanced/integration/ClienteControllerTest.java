package br.com.advanced.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.advanced.application.ClienteController;
import br.com.advanced.domain.dtos.AlterarClienteDTO;
import br.com.advanced.domain.dtos.CriarClienteDTO;
import br.com.advanced.domain.dtos.ObterClienteDTO;
import br.com.advanced.domain.interfaces.ClienteService;

@WebMvcTest(controllers = ClienteController.class)
public class ClienteControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ClienteService clienteService;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("POST /api/v1/clientes - Deve retornar 201 ao cadastrar.")
	public void postClientesReturnsCreated() throws Exception {
	   
	   var dto = new CriarClienteDTO();
       dto.setNome("Fulano Teste");
       dto.setEmail("fulano@teste.com");
       dto.setCpf("12345678900");
       
       var response = new ObterClienteDTO();
       response.setId(UUID.randomUUID());
       response.setNome(dto.getNome());
       response.setEmail(dto.getEmail());
       response.setCpf(dto.getCpf());
       response.setDataHoraCriacao(LocalDateTime.now());
       response.setDataHoraUltimaAlteracao(LocalDateTime.now());
       
       when(clienteService.criar(any(CriarClienteDTO.class))).thenReturn(response);
       
       mockMvc.perform(post("/api/v1/clientes")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.nome").value(response.getNome()))
               .andExpect(jsonPath("$.email").value(response.getEmail()))
       		.andExpect(jsonPath("$.cpf").value(response.getCpf()));
	}
	
	@Test
	@DisplayName("PATCH /api/v1/clientes - Deve retornar 200 ao atualizar.")
	public void patchClientesReturnsOk() throws Exception {
		
	   var dto = new AlterarClienteDTO();
       dto.setId(UUID.randomUUID());
       dto.setNome("Novo Nome");
       dto.setEmail("novo@teste.com");
       dto.setCpf("98765432100");
       
       var response = new ObterClienteDTO();
       response.setId(dto.getId());
       response.setNome(dto.getNome());
       response.setEmail(dto.getEmail());
       response.setCpf(dto.getCpf());
       response.setDataHoraCriacao(LocalDateTime.now().minusDays(1));
       response.setDataHoraUltimaAlteracao(LocalDateTime.now());
       
       when(clienteService.alterar(any(AlterarClienteDTO.class))).thenReturn(response);
       
       mockMvc.perform(patch("/api/v1/clientes")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nome").value(response.getNome()))
               .andExpect(jsonPath("$.email").value(response.getEmail()))
       		.andExpect(jsonPath("$.cpf").value(response.getCpf()));
	}

	
	@Test
	@DisplayName("DELETE /api/v1/clientes - Deve retornar 200 ao excluir.")
	public void deleteClientesReturnsOk() throws Exception {
		
		var id = UUID.randomUUID();
		
       var response = new ObterClienteDTO();
       response.setId(id);
       response.setNome("Cliente Inativo");
       response.setEmail("inativo@teste.com");
       response.setCpf("12345678900");
       
       when(clienteService.inativar(id)).thenReturn(response);
       
       mockMvc.perform(delete("/api/v1/clientes/{id}", id))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nome").value(response.getNome()))
               .andExpect(jsonPath("$.email").value(response.getEmail()))
       		.andExpect(jsonPath("$.cpf").value(response.getCpf()));
	}
	
	@Test
	@DisplayName("GET /api/v1/clientes - Deve retornar 200 ao consultar todos.")
	public void getAllClientesReturnsOk() throws Exception {
		
	   var cliente = new ObterClienteDTO();
       cliente.setId(UUID.randomUUID());
       cliente.setNome("Cliente Paginado");
       cliente.setEmail("paginado@teste.com");
       cliente.setCpf("11122233344");
       
       var page = new PageImpl<>(java.util.List.of(cliente), PageRequest.of(0, 10), 1);
       
       when(clienteService.consultarAtivos(0, 10, "nome", "asc")).thenReturn(page);
       
       mockMvc.perform(get("/api/v1/clientes")
               .param("page", "0")
               .param("size", "10")
               .param("sortBy", "nome")
               .param("direction", "asc"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].nome").value("Cliente Paginado"));
	
     }
	
	@Test
	@DisplayName("GET /api/v1/clientes - Deve retornar 200 ao consultar 1 cliente por ID.")
	public void getByIdClientesReturnsOk() throws Exception {
		
	   var id = UUID.randomUUID();
       
	   var response = new ObterClienteDTO();
       response.setId(id);
       response.setNome("Cliente Por ID");
       response.setEmail("id@teste.com");
       response.setCpf("55566677788");
       
       when(clienteService.obterAtivoPorId(id)).thenReturn(response);
       
       mockMvc.perform(get("/api/v1/clientes/{id}", id))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(id.toString()))
               .andExpect(jsonPath("$.nome").value(response.getNome()))
               .andExpect(jsonPath("$.email").value(response.getEmail()))
       		.andExpect(jsonPath("$.cpf").value(response.getCpf()));
	}
}