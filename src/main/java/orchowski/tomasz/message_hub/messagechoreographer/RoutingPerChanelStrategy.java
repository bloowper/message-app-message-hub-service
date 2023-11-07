package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
class RoutingPerChanelStrategy implements RoutingKeyStrategy {
    private final String routingTemplate;
    private final String destinationChanelUuid;
    private final String serviceUuid;

    @Override
    public String getRoutingKey() {
        return routingTemplate.formatted(serviceUuid, destinationChanelUuid);
    }
}
