package br.com.advanced.infrastructure.repositories;

import br.com.advanced.infrastructure.outbox.OutboxMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {
    @Query("""
            SELECT outbox FROM OutboxMessage outbox
            WHERE outbox.published = false
            AND outbox.type = :param_type
            ORDER BY outbox.createdAt ASC
            """)
    List<OutboxMessage> find(@Param("param_type") String type, Pageable pageable);
}
