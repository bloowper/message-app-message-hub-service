package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.rabbitmq.BindingSpecification;

@Service
@Slf4j
@RequiredArgsConstructor
@ToString
class BindingSpecificationFactory {
    private final Properties properties;

    BindingSpecification createBindingSpecification(String queue, String messageChanelUuid) {
        BindToRoutingKeyStrategy routingKeyStrategy = new BindToRelatedChannelsStrategy(properties.getRoutingKey().getTemplateUserMessage(), messageChanelUuid);
        return BindingSpecification.binding(
                properties.getExchange().getUserMessage(),
                routingKeyStrategy.getRoutingKey(),
                queue
        );
    }

}
