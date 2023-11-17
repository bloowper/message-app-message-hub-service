package orchowski.tomasz.message_hub.messagechoreographer;

import orchowski.tomasz.message_hub.messagechoreographer.api.MessageReceivingException;

class MessageUnmarshallingException extends MessageReceivingException {
    public MessageUnmarshallingException(Throwable cause) {
        super(cause);
    }
}
