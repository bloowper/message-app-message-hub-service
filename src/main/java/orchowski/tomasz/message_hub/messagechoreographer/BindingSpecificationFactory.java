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

    BindingSpecification createBindingSpecification(String queue) {
        BindingSpecification bindingSpecification = new BindingSpecification();
        bindingSpecification.exchange(properties.getExchange().getUserMessage());
        bindingSpecification.queue(queue);
        return bindingSpecification;
    }
}
