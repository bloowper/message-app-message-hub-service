package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import orchowski.tomasz.message_hub.messagechoreographer.api.MessageChoreographerFacade;
import orchowski.tomasz.message_hub.messagechoreographer.api.UserMessageDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

@Service
@RequiredArgsConstructor
@Profile("!messageChoreographer")
public class MessageChoreographerServiceStub implements MessageChoreographerFacade {
    // Current implementation broadcast messages to all clients
    private final Sinks.Many<UserMessageDto> sinksMany = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    @Override
    public Flux<UserMessageDto> getUserMessages(Mono<String> userUuidMono) {
        return sinksMany.asFlux();
    }

    @Override
    public Mono<Void> sendMessage(Mono<UserMessageDto> userMessageMono) {
        return userMessageMono.doOnNext(sinksMany::tryEmitNext).then();
    }

}
