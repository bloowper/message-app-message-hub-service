package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class QueuePerMessageChanelStrategy implements QueueNamingStrategy {
    private final String queueNameTemplate;
    private final String chanelUuid;
    private final String serviceUuid;

    @Override
    public String getQueueName() {
        return queueNameTemplate.formatted(serviceUuid, chanelUuid);
    }
}
