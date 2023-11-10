package orchowski.tomasz.message_hub.messagehandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagechoreographer.MessageChoreographerFacade;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
    // TODO change user-uuid header to jwt token and extract user uuid

    private final MessageChoreographerFacade messageChoreographerFacade;
    private final MessageMapper messageMapper;

    @GetMapping(value = "/message", produces = APPLICATION_NDJSON_VALUE)
    Flux<UserMessageDto> getMessages(@RequestHeader("User-uuid") String userUuid) {
        return messageChoreographerFacade.getUserMessages(Mono.just(userUuid));
    }

    @PostMapping(value = "/message")
    Mono<Void> sendMessage(@RequestBody Mono<MessageFromUser> messageSendByUser, @RequestHeader("User-uuid") String userUuid) {
        return messageSendByUser
                .map(messageFromUser -> messageMapper.toDto(messageFromUser, userUuid))
                .publish(messageChoreographerFacade::sendMessage);
    }

}
