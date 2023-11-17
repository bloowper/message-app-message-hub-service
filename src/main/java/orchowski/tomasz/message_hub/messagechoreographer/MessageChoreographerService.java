package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagechoreographer.api.MessageChoreographerFacade;
import orchowski.tomasz.message_hub.messagechoreographer.api.UserMessageDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Profile("messageChoreographer")
@Slf4j
class MessageChoreographerService implements MessageChoreographerFacade {
    private final MessagingService messagingService;

    @Override
    public Flux<UserMessageDto> getUserMessages(Mono<String> userUuidMono) {
        return messagingService.getUserMessages(userUuidMono);
    }

    @Override
    public Mono<Void> sendMessage(Mono<UserMessageDto> userMessageMono) {
        return messagingService.sendUserMessages(userMessageMono);
    }

}
