package orchowski.tomasz.message_hub.messagechoreographer.adapters.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagechoreographer.adapters.common.MessageFromUser;
import orchowski.tomasz.message_hub.messagechoreographer.adapters.common.MessageMapper;
import orchowski.tomasz.message_hub.messagechoreographer.api.MessageChoreographerFacade;
import orchowski.tomasz.message_hub.messagechoreographer.api.UserMessageDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600) // TODO REMOVE THIS AFTER LOCAL UI DEVELOPMENT
class MessageController {
    private final MessageChoreographerFacade messageChoreographerFacade;
    private final MessageMapper messageMapper;

    @GetMapping(value = "/messages", produces = APPLICATION_NDJSON_VALUE)
    Flux<UserMessageDto> getMessages(@RequestHeader("User-uuid") String userUuid) {
        return messageChoreographerFacade.getUserMessages(Mono.just(userUuid))
                .doOnSubscribe(subscription -> log.info("User {} subscribed to messages", userUuid))
                .doOnCancel(() -> log.info("User {} unsubscribed from messages", userUuid))
                .doOnComplete(() -> log.info("User {} completed messages", userUuid))
                .doOnNext(userMessageDto -> log.info("User {} received message {}", userUuid, userMessageDto))
                .doOnError(throwable -> log.error("User {} error occurred {}", userUuid, throwable.getMessage()));
    }

    @PostMapping(value = "/messages")
    Mono<Void> sendMessage(@RequestBody Mono<MessageFromUser> messageSendByUser, @RequestHeader("User-uuid") String userUuid) {
        return messageSendByUser
                .map(messageFromUser -> messageMapper.toDto(messageFromUser, userUuid))
                .publish(messageChoreographerFacade::sendMessage);
    }

}
