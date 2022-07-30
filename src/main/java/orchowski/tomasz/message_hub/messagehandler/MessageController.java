package orchowski.tomasz.message_hub.messagehandler;

import lombok.RequiredArgsConstructor;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import orchowski.tomasz.message_hub.messagechoreographer.MessageChoreographerFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class MessageController {
    private final MessageChoreographerFacade messageChoreographerFacade;
    private final MessageMapper messageMapper;

    @GetMapping(value = "/message", produces = APPLICATION_NDJSON_VALUE)
    Flux<UserMessageDto> getMessages() {
        return messageChoreographerFacade.getUserMessages(Mono.just("7d7d757b-d9fb-4780-8f04-e784307a2b7a"));
    }

    @PostMapping(value = "/message")
    Mono<Void> sendMessage(@RequestBody Mono<MessageFromUser> messageSendByUser) {
        // TODO 1. Get user uuid from his token and inject to controller
        return messageChoreographerFacade.sendMessage(messageSendByUser.map(messageMapper::toDto));
    }

}
