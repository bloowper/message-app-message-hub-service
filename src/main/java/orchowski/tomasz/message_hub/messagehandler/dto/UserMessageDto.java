package orchowski.tomasz.message_hub.messagehandler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserMessageDto {
    Instant creationDate;
    String chanelUuid;
    String userName;
    String messageContent;
}