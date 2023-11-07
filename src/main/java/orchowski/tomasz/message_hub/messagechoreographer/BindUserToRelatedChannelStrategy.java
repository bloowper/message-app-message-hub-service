package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class BindUserToRelatedChannelStrategy implements BindToRoutingKeyStrategy {
    private static final String INSTANCE = "*";
    private final String routingKeyTemplate;
    private final String messageChanelUuid;

    @Override
    public String getRoutingKey() {
        return routingKeyTemplate.formatted(INSTANCE, messageChanelUuid);
    }
}
