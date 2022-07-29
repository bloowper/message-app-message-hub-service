package orchowski.tomasz.message_hub.messagechoreographer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.configuration.dto.ServiceUuidDto;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
class OutboundMessageFactory {
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    private final ObjectMapper objectMapper;
    private final ServiceUuidDto serviceUuidDto;
    private final Properties properties;

    Mono<OutboundMessage> createOutboundMessageFlux(Mono<UserMessageDto> userMessageDtoFlux) {
        return userMessageDtoFlux.map(
                userMessageDto -> {
                    try {
                        RoutingKeyStrategy routingKeyStrategy = gerRoutingKeyStrategy(userMessageDto.getDestinationChanelUuid());
                        String marshaledMessage = objectMapper.writeValueAsString(userMessageDto);
                        return new OutboundMessage(
                                properties.getExchange().getUserMessage(),
                                routingKeyStrategy.getRoutingKey(),
                                marshaledMessage.getBytes(CHARSET)
                        );
                    } catch (JsonProcessingException e) {
                        throw new MessageMarshalingException(e);
                    }
                }
        );
    }


    private RoutingKeyStrategy gerRoutingKeyStrategy(String destinationChanelUuid) {
        return new RoutingPerChanelStrategy(properties.getRoutingKey().getTemplateUserMessage(), destinationChanelUuid, serviceUuidDto.uuid());
    }

}
