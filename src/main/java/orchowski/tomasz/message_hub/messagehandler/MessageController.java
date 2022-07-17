package orchowski.tomasz.message_hub.messagehandler;

import lombok.RequiredArgsConstructor;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import orchowski.tomasz.message_hub.messageorchestration.MessageOrchestrationFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
class MessageController {
    private final MessageOrchestrationFacade messageOrchestrationFacade;
    private final MessageMapper messageMapper;

    @GetMapping(value = "/message", produces = APPLICATION_NDJSON_VALUE)
    Flux<UserMessageDto> getMessages() {
        return Flux.just(new UserMessageDto(Instant.now(), UUID.randomUUID().toString(), "username", UUID.randomUUID().toString(), "message content"));
    }

    @PostMapping(value = "/message")
    Mono<Void> sendMessage(@RequestBody Mono<MessageFromUser> messageSendByUser) {
        // TODO 1. Get user uuid from his token and inject to controller
        return null;
    }

}
