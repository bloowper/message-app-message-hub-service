package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.rabbitmq.BindingSpecification;

@Service
@Slf4j
@RequiredArgsConstructor
class BindingSpecificationFactory {
    private final Properties properties;

    BindingSpecification createBindingSpecification(String queue, String messageChanelUuid) {
        BindToRoutingKeyStrategy routingKeyStrategy = new BindToRelatedChannelsStrategy(properties.getRoutingKey().getTemplateUserMessage(), messageChanelUuid);
        BindingSpecification bindingSpecification = new BindingSpecification();
        bindingSpecification.exchange(properties.getExchange().getUserMessage());
        bindingSpecification.queue(queue);
        bindingSpecification.routingKey(routingKeyStrategy.getRoutingKey());
        return bindingSpecification;
    }
}
