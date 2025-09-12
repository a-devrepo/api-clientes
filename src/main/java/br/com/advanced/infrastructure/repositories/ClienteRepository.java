package br.com.advanced.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.advanced.domain.entities.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente,UUID> {
	
	boolean existsByCpf(String cpf);
	
	boolean existsByEmail(String email);
	
	Optional<Cliente> findByCpf(String cpf);
	
	Optional<Cliente> findByEmail(String email);
	
	Page<Cliente> findByAtivoTrue(Pageable pageable);
}