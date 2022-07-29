package orchowski.tomasz.message_hub.messagechoreographer;

import orchowski.tomasz.message_hub.messagechoreographer.dto.MessageNotSendException;

class MessageMarshalingException extends MessageNotSendException {
    public MessageMarshalingException(Throwable cause) {
        super(cause);
    }
}
