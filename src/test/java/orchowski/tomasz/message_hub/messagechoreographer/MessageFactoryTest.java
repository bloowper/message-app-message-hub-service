package orchowski.tomasz.message_hub.messagechoreographer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import orchowski.tomasz.message_hub.configuration.dto.ServiceUuidDto;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.rabbitmq.OutboundMessage;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class MessageFactoryTest {
    private static final JavaTimeModule JAVA_TIME_MODULE = new JavaTimeModule(); // JACKSON dont support java 8 data types by default
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(JAVA_TIME_MODULE);
    private static final ServiceUuidDto SERVICE_UUID_DTO = new ServiceUuidDto(UUID.randomUUID().toString());
    public static final Properties PROPERTIES = new Properties(
            new Properties.Exchange(
                    "tx.user.message"
            ),
            new Properties.Queue(
                    "message-hub.instance.%s.message-chanel.%s"
            ),
            new Properties.RoutingKey(
                    "instance.%s.destination-message-chanel.%s"
            )
    );
    private static final MessageFactory MESSAGE_FACTORY = new MessageFactory(OBJECT_MAPPER, SERVICE_UUID_DTO, PROPERTIES);

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
        Flux<OutboundMessage> outboundMessageFlux = MESSAGE_FACTORY.createOutboundMessageFlux(Flux.just(userMessageDto));

        // then
        StepVerifier.create(outboundMessageFlux)
                .assertNext(outboundMessage -> {
                    assertEquals(
                            new RoutingPerChanelStrategy(PROPERTIES.getRoutingKey().getTemplateUserMessage(), destinationChanelUuid, SERVICE_UUID_DTO.uuid()).getRoutingKey(),
                            outboundMessage.getRoutingKey()
                    );
                    assertEquals(
                            PROPERTIES.getExchange().getUserMessage(),
                            outboundMessage.getExchange()
                    );
                    //TODO provide tests for body of rabbitmq message
                }).verifyComplete();
    }
}