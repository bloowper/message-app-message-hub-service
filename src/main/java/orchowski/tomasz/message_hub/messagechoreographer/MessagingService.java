package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
@RequiredArgsConstructor
@Slf4j
class MessagingService {
    private final MessageFactory messageFactory;

    Flux<Void> sendUserMessages(Flux<UserMessageDto> userMessageDtoFlux) {
        return null;
    }


}
