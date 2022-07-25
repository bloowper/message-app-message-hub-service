package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Profile("messageChoreographer")
@Slf4j
public class MessageChoreographerService implements MessageChoreographerFacade {

    @Override
    public Flux<UserMessageDto> getMessagesToUser(Mono<String> userUuidMono) {
        return null;
    }

    @Override
    public Mono<Void> sendMessage(Mono<UserMessageDto> userMessageMono) {
        return null;
    }

}
