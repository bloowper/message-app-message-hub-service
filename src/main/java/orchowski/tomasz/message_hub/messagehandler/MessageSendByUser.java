package orchowski.tomasz.message_hub.messagehandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class MessageSendByUser {
    String userUuid;
    String destinationChanelUuid;
    String messageContent;
}