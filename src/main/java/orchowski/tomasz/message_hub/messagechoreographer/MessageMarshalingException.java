package orchowski.tomasz.message_hub.messagechoreographer;

import orchowski.tomasz.message_hub.messagechoreographer.api.MessageSendingException;

class MessageMarshalingException extends MessageSendingException {
    public MessageMarshalingException(Throwable cause) {
        super(cause);
    }
}
