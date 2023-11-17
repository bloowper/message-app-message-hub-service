package orchowski.tomasz.message_hub.messagehandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import orchowski.tomasz.message_hub.messagechoreographer.MessageChoreographerFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
class MessageWebSocketConfiguration {
    private final MessageChoreographerFacade messageChoreographerFacade;
    private final MessageMapper messageMapper;
    private final ObjectMapper objectMapper;
    @Bean
    HandlerMapping handlerMapping() {
        Map<String, MessageWebSocketHandler> mappings = Map.of(
                "/api/v2/messages", new MessageWebSocketHandler(messageChoreographerFacade, messageMapper, objectMapper)
        );
        return new SimpleUrlHandlerMapping(mappings, -1); // before annotated controllers
    }

    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
