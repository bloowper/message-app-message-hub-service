package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagechoreographer.dto.MessageNotSendException;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.Sender;


@Service
@RequiredArgsConstructor
@Slf4j
class MessagingService {
    private final OutboundMessageFactory outboundMessageFactory;
    private final Sender sender;

    Mono<Void> sendUserMessages(Mono<UserMessageDto> userMessageDtoFlux) {
        return sender.send(outboundMessageFactory.createOutboundMessageFlux(userMessageDtoFlux))
                .onErrorMap(MessageNotSendException::new);
    }


}
