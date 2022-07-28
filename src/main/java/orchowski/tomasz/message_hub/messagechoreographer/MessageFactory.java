package orchowski.tomasz.message_hub.messagechoreographer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.configuration.dto.ServiceUuidDto;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.rabbitmq.OutboundMessage;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
class MessageFactory {
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    private final ObjectMapper objectMapper;
    private final ServiceUuidDto serviceUuidDto;
    private final Properties properties;

    Flux<OutboundMessage> createOutboundMessageFlux(Flux<UserMessageDto> userMessageDtoFlux) {
        return userMessageDtoFlux.map(
                userMessageDto -> {
                    try {
                        RoutingKeyStrategy routingPerUerStrategy = getRoutingPerUerStrategy(userMessageDto.getDestinationChanelUuid());
                        String marshaledMessage = objectMapper.writeValueAsString(userMessageDto);
                        return new OutboundMessage(
                                properties.getExchange().getUserMessage(),
                                routingPerUerStrategy.getRoutingKey(),
                                marshaledMessage.getBytes(CHARSET)
                        );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);// Just sneaky throw, next element chain handle this
                    }
                }
        ).onErrorContinue((throwable, o) -> {
            log.warn("Marshalling of object [{}] failed", o, throwable);
        });
    }


    private RoutingKeyStrategy getRoutingPerUerStrategy(String destinationChanelUuid) {
        return new RoutingPerChanelStrategy(properties.getRoutingKey().getTemplateUserMessage(), destinationChanelUuid, serviceUuidDto.uuid());
    }

}
