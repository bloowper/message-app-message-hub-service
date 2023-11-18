package orchowski.tomasz.message_hub.messagechoreographer.adapters.ws;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagechoreographer.adapters.common.MessageFromUser;
import orchowski.tomasz.message_hub.messagechoreographer.adapters.common.MessageMapper;
import orchowski.tomasz.message_hub.messagechoreographer.api.MessageChoreographerFacade;
import orchowski.tomasz.message_hub.messagechoreographer.api.UserMessageDto;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
class MessageWebSocketHandler implements WebSocketHandler {
    private final MessageChoreographerFacade messageChoreographerFacade;
    private final MessageMapper messageMapper;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Optional<String> optionalUserUuid = getUserUuid(session);
        if(optionalUserUuid.isEmpty()) {
            return session.close();
        }
        String userUuid = optionalUserUuid.get();
        Mono<Void> inbound = getInboundMessageConsumer(session, userUuid);
        Mono<Void> outbound = getOutboundMessageProducer(session, userUuid);
        return Mono.zip(inbound, outbound).then();
    }

    private Mono<Void> getInboundMessageConsumer(WebSocketSession session, String userUuid) {
        return session.receive()
                .map(this::deserializeInboundMessage)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(messageFromUser -> messageMapper.toDto(messageFromUser, userUuid))
                .map(Mono::just)
                .flatMap(messageChoreographerFacade::sendMessage)
                .then();
    }


    private Mono<Void> getOutboundMessageProducer(WebSocketSession session, String userUuid) {
        return session.send(
                messageChoreographerFacade.getUserMessages(Mono.just(userUuid))
                        .map(userMessageDto -> serializeOutboundMessage(userMessageDto, session))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
        );
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

    private Optional<String> getUserUuid(WebSocketSession session) {
        List<String> uuidlist = session.getHandshakeInfo().getHeaders().get("User-Uuid");
        if (uuidlist!=null && !uuidlist.isEmpty()) {
            return Optional.of(uuidlist.get(0));
        }
        return Optional.empty();
    }
}
