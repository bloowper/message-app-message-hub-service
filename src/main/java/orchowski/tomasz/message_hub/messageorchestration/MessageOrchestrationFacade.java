package orchowski.tomasz.message_hub.messageorchestration;

import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageOrchestrationFacade {
    Flux<UserMessageDto> getMessagesToUser(Mono<String> userUuidMono);

    Mono<Void> sendMessage(Mono<UserMessageDto> userMessageMono);
}