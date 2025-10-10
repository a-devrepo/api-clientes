package br.com.advanced.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record NovoClienteEvent(
        UUID id,
        String nome,
        String email,
        String cpf,
        LocalDateTime dataHoraCadastro
) {
}