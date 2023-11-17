package orchowski.tomasz.message_hub.messagechoreographer.adapters.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageToUser {
    Instant creationDate;
    String destinationChanelUuid;
    String userName;
    String userUuid;
    String messageContent;
}
