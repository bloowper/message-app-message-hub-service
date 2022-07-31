package orchowski.tomasz.message_hub.messagechoreographer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Delivery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagechoreographer.dto.MessageNotSendException;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
class MessagingService {
    //TODO REFACTOR
    // 1. extract methods
    // 2. extract responsibilities from getUserMessages
    private final Sender sender;
    private final Receiver receiver;
    private final OutboundMessageFactory outboundMessageFactory;
    private final QueueSpecificationFactory queueSpecificationFactory;
    private final BindingSpecificationFactory bindingSpecificationFactory;
    private final ObjectMapper objectMapper;

    Mono<Void> sendUserMessages(Mono<UserMessageDto> userMessageDtoFlux) {
        return sender.send(outboundMessageFactory.createOutboundMessageFlux(userMessageDtoFlux))
                .onErrorMap(MessageNotSendException::new);
    }


    public Flux<UserMessageDto> getUserMessages(Mono<String> userUuidMono) {
        // TODO steps
        // 0. fetch user related channels
        // 1 . create queue
        // 2. bind queue to topics that are for related channles
        // 3. subscribe to queue
        return userUuidMono
                .map(userUuid -> {
                    return new QueueSpecificationWithUserUuid(
                            queueSpecificationFactory.createSpecification(userUuid, UUID.randomUUID().toString()),
                            userUuid
                    );
                })
                .doOnNext(queueSpecificationWithUserUuid -> log.info("Creating queue {} for user {}", queueSpecificationWithUserUuid.queueSpecification().getName(), queueSpecificationWithUserUuid.userUuid))
                .flatMapMany(queueSpecificationWithUserUuid -> {
                    return sender.declareQueue(queueSpecificationWithUserUuid.queueSpecification())
                            .doOnNext($ -> log.info("Queue {} for user {} created",queueSpecificationWithUserUuid.queueSpecification().getName(),queueSpecificationWithUserUuid.userUuid()))
                            .doOnNext($ -> log.info("Binding process started"))
                            .then(
                                    getUserChannels(queueSpecificationWithUserUuid.userUuid())
                                            .map(messageChanelUuid -> bindingSpecificationFactory.createBindingSpecification(queueSpecificationWithUserUuid.queueSpecification.getName(), messageChanelUuid))
                                            .doOnNext(bindingSpecification -> log.info("Binding queue {} to exchange {} by routing key {}",bindingSpecification.getQueue(),bindingSpecification.getExchange(),bindingSpecification.getRoutingKey()))
                                            .flatMap(sender::bind)
                                            .doOnNext(bindingSpecification -> log.info("Queue bound"))
                                            .then()
                            )
                            .doOnNext(unused -> log.info("Binding process ended"))
                            .doOnNext(bindingSpecification -> log.info("Starting message consuming"))
                            .thenMany(receiver.consumeAutoAck(Objects.requireNonNull(queueSpecificationWithUserUuid.queueSpecification.getName())))
                            .map(Delivery::getBody)
                            .map(this::unmarshallMessage);
                });

    }

    private UserMessageDto unmarshallMessage(byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, UserMessageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Flux<String> getUserChannels(String userUuid) {
        // TODO
        // THIS IS MOCK AT THIS POINT
        Map<String, Flux<String>> userUuidChannelsMap = Map.of(
                "7d7d757b-d9fb-4780-8f04-e784307a2b7a", Flux.just("99b1ace7-dc61-49a6-8e54-bfafb54f4fac", "c1b61767-effa-439d-aa97-c0edc47d326e", "d291c024-de26-4ad4-80aa-ab9b77f4105b"),
                "d7b8cfd6-efca-4c3e-9ed3-63d74c80214d", Flux.just("99b1ace7-dc61-49a6-8e54-bfafb54f4fac")
        );
        return userUuidChannelsMap.get(userUuid);
    }

    private record QueueSpecificationWithUserUuid(QueueSpecification queueSpecification, String userUuid) {
    }
}
