package br.com.advanced.infrastructure.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.advanced.domain.entities.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,UUID> {}