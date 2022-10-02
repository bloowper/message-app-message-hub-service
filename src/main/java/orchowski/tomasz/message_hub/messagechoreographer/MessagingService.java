package orchowski.tomasz.message_hub.messagechoreographer;

import com.rabbitmq.client.Delivery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagechoreographer.dto.MessageSendingException;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import orchowski.tomasz.message_hub.userinformation.UserInformationFacade;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
class MessagingService {
    private final Sender sender;
    private final Receiver receiver;
    private final UserInformationFacade userInformationFacade;
    private final OutboundMessageFactory outboundMessageFactory;
    private final QueueSpecificationFactory queueSpecificationFactory;
    private final BindingSpecificationFactory bindingSpecificationFactory;
    private final UserMessageFactory userMessageFactory;

    Mono<Void> sendUserMessages(Mono<UserMessageDto> userMessageDtoFlux) {
        return sender.send(outboundMessageFactory.createOutboundMessageFlux(userMessageDtoFlux))
                .onErrorMap(MessageSendingException::new);
    }


    Flux<UserMessageDto> getUserMessages(Mono<String> userUuidMono) {
        return userUuidMono
                .map(this::createQueueSpecification)
                .doOnNext(queueSpecificationWithUserUuid -> log.info("Creating queue {} for user {}", queueSpecificationWithUserUuid.queueSpecification().getName(), queueSpecificationWithUserUuid.userUuid))
                .flatMapMany(this::createQueueAndSubscribeTo);
    }

    private QueueSpecificationWithUserUuid createQueueSpecification(String userUuid) {
        return new QueueSpecificationWithUserUuid(queueSpecificationFactory.createSpecification(userUuid, UUID.randomUUID().toString()), userUuid);
    }

    private Flux<UserMessageDto> createQueueAndSubscribeTo(QueueSpecificationWithUserUuid queueSpecificationWithUserUuid) {
        return sender.declareQueue(queueSpecificationWithUserUuid.queueSpecification())
                .doOnNext($ -> log.info("Created queue {} for user {}", queueSpecificationWithUserUuid.queueSpecification().getName(), queueSpecificationWithUserUuid.userUuid()))
                .doOnNext($ -> log.info("Binding process started"))
                .then(bindQueueToRelatedChannels(queueSpecificationWithUserUuid))
                .doOnNext($ -> log.info("Starting message consuming"))
                .thenMany(receiver.consumeAutoAck(Objects.requireNonNull(queueSpecificationWithUserUuid.queueSpecification.getName())))
                .map(Delivery::getBody)
                .map(userMessageFactory::createUserMessage)
                .onErrorContinue(MessageUnmarshallingException.class, (throwable, o) -> log.info("Message unmarshalling failed", throwable));
    }

    private Mono<Void> bindQueueToRelatedChannels(QueueSpecificationWithUserUuid queueSpecificationWithUserUuid) {
        return userInformationFacade.getUserChannels(Mono.just(queueSpecificationWithUserUuid.userUuid()))
                .map(channelDto -> bindingSpecificationFactory.createBindingSpecification(queueSpecificationWithUserUuid.queueSpecification.getName(), channelDto.chanelId()))
                .doOnNext(bindingSpecification -> log.info("Binding queue {} to exchange {} by routing key {}", bindingSpecification.getQueue(), bindingSpecification.getExchange(), bindingSpecification.getRoutingKey()))
                .flatMap(sender::bind)
                .doOnNext(bindingSpecification -> log.info("Queue bound"))
                .then();
    }

    private record QueueSpecificationWithUserUuid(QueueSpecification queueSpecification, String userUuid) {
    }
}
