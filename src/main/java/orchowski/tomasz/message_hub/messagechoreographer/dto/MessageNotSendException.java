package orchowski.tomasz.message_hub.messagechoreographer.dto;

public class MessageNotSendException extends RuntimeException {

    public MessageNotSendException(Throwable cause) {
        super(cause);
    }
}
