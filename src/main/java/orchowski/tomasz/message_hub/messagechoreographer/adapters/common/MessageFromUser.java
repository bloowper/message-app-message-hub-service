package orchowski.tomasz.message_hub.messagechoreographer.adapters.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageFromUser {
    String destinationChanelUuid;
    String messageContent;
}
