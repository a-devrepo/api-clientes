package br.com.advanced.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(length = 150, nullable = false)
	private String nome;
	
	@Column(length = 100, nullable = false, unique = true)
	private String email;
	
	@Column(length = 11, nullable = false, unique = true)
	private String cpf;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dataHoraCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dataHoraUltimaAlteracao;
	
	@Column(nullable = false)
	private Boolean ativo;
}