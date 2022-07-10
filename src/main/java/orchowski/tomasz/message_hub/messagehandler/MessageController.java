package orchowski.tomasz.message_hub.messagehandler;

import orchowski.tomasz.message_hub.messagehandler.dto.UserMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

@RestController
@RequestMapping("/api/v1")
class MessageController {

    @GetMapping(value = "/message", produces = APPLICATION_NDJSON_VALUE)
    Flux<UserMessage> getMessages() {
        return Flux.just(
                new UserMessage(Instant.now(), UUID.randomUUID().toString(), "user1", "message 0 content"),
                new UserMessage(Instant.now(), UUID.randomUUID().toString(), "user1", "message 1 content"),
                new UserMessage(Instant.now(), UUID.randomUUID().toString(), "user1", "message 2 content"),
                new UserMessage(Instant.now(), UUID.randomUUID().toString(), "user1", "message 3 content"),
                new UserMessage(Instant.now(), UUID.randomUUID().toString(), "user1", "message 4 content"),
                new UserMessage(Instant.now(), UUID.randomUUID().toString(), "user1", "message 5 content"),
                new UserMessage(Instant.now(), UUID.randomUUID().toString(), "user1", "message 6 content")
        ).delayElements(Duration.ofSeconds(1));
    }

}
