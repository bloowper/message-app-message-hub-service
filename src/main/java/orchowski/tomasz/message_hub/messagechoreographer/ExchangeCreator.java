package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ExchangeCreator {
    // This configures infrastructure after application startup( creating exchange in rabbitmq) [not shore if it is good practice]
    private final TopicExchange userMessageBrokerTopicExchange;
    private final AmqpAdmin amqpAdmin;

    @EventListener
    public void createExchangeOnApplicationReady(ApplicationReadyEvent applicationReadyEvent) {
        amqpAdmin.declareExchange(userMessageBrokerTopicExchange);
    }
}
