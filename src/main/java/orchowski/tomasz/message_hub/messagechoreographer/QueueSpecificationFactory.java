package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.configuration.dto.ServiceUuidDto;
import org.springframework.stereotype.Service;
import reactor.rabbitmq.QueueSpecification;

@Service
@RequiredArgsConstructor
@Slf4j
class QueueSpecificationFactory {
    private static final boolean AUTO_DELETE = true;
    public static final boolean DURABLE = false;
    private final Properties properties;
    private final ServiceUuidDto serviceUuidDto;

    QueueSpecification createSpecification(String userUuid, String connectionUuid) {
        QueueNamingStrategy queueNamingStrategy = new QueuePerUserStrategy(properties.getQueue().getTemplateUserMessage(), userUuid, serviceUuidDto.uuid(), connectionUuid);
        QueueSpecification queueSpecification = new QueueSpecification();
        queueSpecification.name(queueNamingStrategy.getQueueName());
        queueSpecification.autoDelete(AUTO_DELETE);
        queueSpecification.durable(DURABLE);
        return queueSpecification;
    }
}
