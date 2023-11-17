package orchowski.tomasz.message_hub.messagehandler;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagechoreographer.MessageChoreographerFacade;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
class MessageWebSocketHandler implements WebSocketHandler {
    private final MessageChoreographerFacade messageChoreographerFacade;
    private final MessageMapper messageMapper;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        // TODO: talk about problem with path variables user uuids/arguments etc
        var userUuid = "USER UUID";

        var inbound = session.receive()
                .map(this::deserializeInboundMessage)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(messageFromUser -> messageMapper.toDto(messageFromUser, userUuid)) // path variable user uuid
                .map(Mono::just)
                .flatMap(messageChoreographerFacade::sendMessage)
                .then();

        Mono<Void> outbound = session.send(
                messageChoreographerFacade.getUserMessages(Mono.just(userUuid))
                .map(userMessageDto -> serializeOutboundMessage(userMessageDto, session))
                .filter(Optional::isPresent)
                .map(Optional::get)
        );

        return Mono.zip(inbound, outbound).then();
    }

    private Optional<MessageFromUser> deserializeInboundMessage(WebSocketMessage webSocketMessage) {
        try {
            return Optional.ofNullable(objectMapper.readValue(webSocketMessage.getPayloadAsText(), MessageFromUser.class));
        } catch (JsonProcessingException e) {
            log.warn("Cannot deserialize message {}", webSocketMessage.getPayloadAsText(), e);
        }
        return Optional.empty();
    }

    private Optional<WebSocketMessage> serializeOutboundMessage(UserMessageDto userMessageDto, WebSocketSession session) {
        try {
            return Optional.ofNullable(objectMapper.writeValueAsString(userMessageDto)).map(session::textMessage);
        } catch (JsonProcessingException e) {
            log.warn("Cannot serialize message {}", userMessageDto, e);
        }
        return Optional.empty();
    }
}
