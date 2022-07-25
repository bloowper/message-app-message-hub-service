package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class MessageChoreographerConfiguration {
    // TODO create exchange at application startup (if not exist yet)
    private static final String USER_MESSAGE_EXCHANGE_NAME = "tx.user.messages";


}
