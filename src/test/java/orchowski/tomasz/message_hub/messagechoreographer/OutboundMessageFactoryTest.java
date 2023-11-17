package orchowski.tomasz.message_hub.messagechoreographer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import orchowski.tomasz.message_hub.configuration.dto.ServiceUuidDto;
import orchowski.tomasz.message_hub.messagechoreographer.api.UserMessageDto;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OutboundMessageFactoryTest {
    private static final JavaTimeModule JAVA_TIME_MODULE = new JavaTimeModule(); // JACKSON dont support java 8 data types by default
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(JAVA_TIME_MODULE);
    private static final ServiceUuidDto SERVICE_UUID_DTO = new ServiceUuidDto(UUID.randomUUID().toString());
    public static final MessageBrokerProperties MESSAGE_BROKER_PROPERTIES = new MessageBrokerProperties(
            new MessageBrokerProperties.Exchange(
                    "tx.user.message"
            ),
            new MessageBrokerProperties.Queue(
                    "message-hub.instance.%s.message-chanel.%s"
            ),
            new MessageBrokerProperties.RoutingKey(
                    "instance.%s.destination-message-chanel.%s"
            )
    );
    private static final OutboundMessageFactory MESSAGE_FACTORY = new OutboundMessageFactory(OBJECT_MAPPER, SERVICE_UUID_DTO, MESSAGE_BROKER_PROPERTIES);

    @Test
    void shouldCreateOneOutboundMessage() {
        // given
        var now = Instant.now();
        var destinationChanelUuid = "destinationChanelUuid";
        var username = "username";
        var userUuid = "userUuid";
        var message_content = "Message content";
        var userMessageDto = new UserMessageDto(now, destinationChanelUuid, username, userUuid, message_content);


        // when
        Mono<OutboundMessage> outboundMessageFlux = MESSAGE_FACTORY.createOutboundMessageMono(Mono.just(userMessageDto));

        // then
        StepVerifier.create(outboundMessageFlux)
                .assertNext(outboundMessage -> {
                    assertEquals(
                            new RoutingPerChanelStrategy(MESSAGE_BROKER_PROPERTIES.getRoutingKey().getTemplateUserMessage(), destinationChanelUuid, SERVICE_UUID_DTO.uuid()).getRoutingKey(),
                            outboundMessage.getRoutingKey()
                    );
                    assertEquals(
                            MESSAGE_BROKER_PROPERTIES.getExchange().getUserMessage(),
                            outboundMessage.getExchange()
                    );
                    // TODO provide tests for body of rabbitmq message
                }).verifyComplete();
    }
}
