package br.com.advanced.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CriarClienteDTO {

	@Size(min = 8, max = 150, message = "Mínimo de 8 e máximo de 100 caracteres.")
	@NotBlank(message= "Campo obrigatório.")
	private String nome;
	
	@Email(message = "Formato de email inválido.")
	@NotBlank(message= "Campo obrigatório.")
	private String email;
	
	
	@Pattern(regexp = "^[0-9]{11}$", message = "CPF deve ter 11 números.")
	@NotBlank(message = "Campo obrigatório.")
	private String cpf;
}