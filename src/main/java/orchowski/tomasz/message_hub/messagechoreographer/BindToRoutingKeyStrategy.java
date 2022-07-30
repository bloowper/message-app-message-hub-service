package orchowski.tomasz.message_hub.messagechoreographer;

interface BindToRoutingKeyStrategy {
    String getRoutingKey();
}