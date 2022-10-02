package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({MessageBrokerProperties.class})
class MessageChoreographerConfiguration {
    @Bean
    TopicExchange userMessageBrokerTopicExchange(@Value("${message-broker.exchange.user-message}") String topicName,
                                                 @Value("${message-broker.exchange.durable}") boolean durable,
                                                 @Value("${message-broker.exchange.auto-delete}") boolean autoDelete) {
        return new TopicExchange(topicName, durable, autoDelete);
    }

}
