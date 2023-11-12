package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "message-broker")
@Getter
class MessageBrokerProperties {
    private final Exchange exchange;
    private final Queue queue;
    private final RoutingKey routingKey;

    @ConstructorBinding
    public MessageBrokerProperties(Exchange exchange, Queue queue, RoutingKey routingKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
    }

    @Getter
    @AllArgsConstructor
    static class Exchange {
        private String userMessage;
    }

    @Getter
    @AllArgsConstructor
    static class Queue {
        private String templateUserMessage;
    }

    @Getter
    @AllArgsConstructor
    static class RoutingKey {
        private String templateUserMessage;
    }
}
