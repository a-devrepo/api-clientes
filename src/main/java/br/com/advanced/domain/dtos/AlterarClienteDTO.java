package br.com.advanced.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AlterarClienteDTO {

	@NotNull(message = "Campo obrigatório.")
	private UUID id;
	
	@Size(min = 8, max = 150, message = "Mínimo de 8 e máximo de 100 caracteres.")
	private String nome;
	
	@Email(message = "Formato de email inválido.")
	private String email;
	
	@Pattern(regexp = "^[0-9]{11}$", message = "CPF deve ter 11 números.")
	private String cpf;
}