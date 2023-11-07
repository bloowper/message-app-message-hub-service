package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "message-broker")
@Getter
@AllArgsConstructor
@ConstructorBinding
class MessageBrokerProperties {
    private final Exchange exchange;
    private final Queue queue;
    private final RoutingKey routingKey;

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
