package br.com.advanced.infrastructure.outbox;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table
public class OutboxMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aggregateType;
    private String aggregateId;
    private String type;
    private String payload;
    private boolean published = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime transmittedAt;
}
