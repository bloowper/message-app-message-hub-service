package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class QueuePerUserStrategy implements QueueNamingStrategy {
    private static final String QUEUE_PATTERN = "message-hub.%s.user.%s";
    private final String userUuid;
    private final String serviceUuid;

    @Override
    public String getQueueName() {
        return QUEUE_PATTERN.formatted(serviceUuid, userUuid);
    }
}
