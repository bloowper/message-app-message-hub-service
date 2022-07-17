package orchowski.tomasz.message_hub.messageorchestration;

import lombok.RequiredArgsConstructor;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

@Service
@RequiredArgsConstructor
@Profile("!messageOrchestration")
public class MockMessageOrchestrationService implements MessageOrchestrationFacade {
    private final Sinks.Many<UserMessageDto> sinksMany = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    @Override
    public Flux<UserMessageDto> getMessagesToUser(Mono<String> userUuidMono) {
        return sinksMany.asFlux();
    }

    @Override
    public Mono<Void> sendMessage(Mono<UserMessageDto> userMessageMono) {
        return userMessageMono.doOnNext(sinksMany::tryEmitNext)
                .then();
    }

}
