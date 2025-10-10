package br.com.advanced.infrastructure.components;

import br.com.advanced.infrastructure.repositories.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MessageProducer {
    private final RabbitTemplate rabbitTemplate;
    private final OutboxMessageRepository outboxMessageRepository;
    private final Queue queue;

    @Scheduled(fixedDelay = 60000)
    public void sendMessage() {

        var pageable = PageRequest.of(0, 10);
        var mensagens = outboxMessageRepository.find("NovoCliente", pageable);

        try {
            for(var item : mensagens) {
                rabbitTemplate.convertAndSend(queue.getName(), item.getPayload());

                item.setPublished(true);
                item.setTransmittedAt(LocalDateTime.now());

                outboxMessageRepository.save(item);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
