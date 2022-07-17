package orchowski.tomasz.message_hub.messageorchestration;

import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageOrchestrationFacade {
    Flux<UserMessageDto> getMessagesToUser(String userUuid);

    Mono<Void> sendMessage(UserMessageDto userMessageDto);
}
