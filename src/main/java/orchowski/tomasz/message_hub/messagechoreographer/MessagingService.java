package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import orchowski.tomasz.message_hub.configuration.dto.ServiceUuidDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class MessagingService {
    private final ServiceUuidDto serviceUuidDto;

    QueueNamingStrategy getQueueNamingStrategyForUser(String userUuid) {
        return new QueuePerUserStrategy(userUuid, serviceUuidDto.uuid());
    }

}
