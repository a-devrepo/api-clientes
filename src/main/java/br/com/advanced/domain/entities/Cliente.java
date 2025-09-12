package br.com.advanced.domain.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Cliente {

	private UUID id;
	private String nome;
	private String email;
	private String cpf;
	private LocalDateTime dataHoraCriacao;
	private LocalDateTime dataHoraUltimaAlteracao;
	private Boolean ativo;
	
	public Cliente() {}

	public Cliente(UUID id, String nome, String email, String cpf, LocalDateTime dataHoraCriacao,
			LocalDateTime dataHoraUltimaAlteracao, Boolean ativo) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.cpf = cpf;
		this.dataHoraCriacao = dataHoraCriacao;
		this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
		this.ativo = ativo;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDateTime getDataHoraCriacao() {
		return dataHoraCriacao;
	}

	public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
		this.dataHoraCriacao = dataHoraCriacao;
	}

	public LocalDateTime getDataHoraUltimaAlteracao() {
		return dataHoraUltimaAlteracao;
	}

	public void setDataHoraUltimaAlteracao(LocalDateTime dataHoraUltimaAlteracao) {
		this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nome=" + nome + ", email=" + email + ", cpf=" + cpf + ", dataHoraCriacao="
				+ dataHoraCriacao + ", dataHoraUltimaAlteracao=" + dataHoraUltimaAlteracao + ", ativo=" + ativo + "]";
	}
}