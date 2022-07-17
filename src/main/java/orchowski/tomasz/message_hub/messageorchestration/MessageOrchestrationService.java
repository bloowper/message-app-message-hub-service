package orchowski.tomasz.message_hub.messageorchestration;

import lombok.RequiredArgsConstructor;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Profile("messageOrchestration")
public class MessageOrchestrationService implements MessageOrchestrationFacade {
    @Override
    public Flux<UserMessageDto> getMessagesToUser(Mono<String> userUuidMono) {
        return null;
    }

    @Override
    public Mono<Void> sendMessage(Mono<UserMessageDto> userMessageMono) {
        return null;
    }
}
