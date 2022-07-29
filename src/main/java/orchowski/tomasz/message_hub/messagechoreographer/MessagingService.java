package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagechoreographer.dto.MessageNotSendException;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;


@Service
@RequiredArgsConstructor
@Slf4j
class MessagingService {
    private final OutboundMessageFactory outboundMessageFactory;
    private final Sender sender;
    private final Receiver receiver;

    Mono<Void> sendUserMessages(Mono<UserMessageDto> userMessageDtoFlux) {
        return sender.send(outboundMessageFactory.createOutboundMessageFlux(userMessageDtoFlux))
                .onErrorMap(MessageNotSendException::new);
    }


    public Flux<UserMessageDto> getUserMessages(Mono<String> userUuidMono) {
        // TODO
        // 0. fetch user related channels
        // 1 . create queue
        // 2. bind queue to topics that are for related channles
        // 3. subscribe to queue


        return Flux.empty();
    }
}
