package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class QueuePerUserStrategy implements QueueNamingStrategy {
    private final String queueNameTemplate;
    private final String userUuid;
    private final String serviceUuid;
    private final String connectionUuid;

    @Override
    public String getQueueName() {
        return queueNameTemplate.formatted(serviceUuid, userUuid, connectionUuid);
    }

}
