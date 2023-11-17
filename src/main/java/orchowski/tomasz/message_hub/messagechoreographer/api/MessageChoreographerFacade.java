package orchowski.tomasz.message_hub.messagechoreographer.api;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageChoreographerFacade {
    Flux<UserMessageDto> getUserMessages(Mono<String> userUuidMono);

    Mono<Void> sendMessage(Mono<UserMessageDto> userMessageMono);
}
