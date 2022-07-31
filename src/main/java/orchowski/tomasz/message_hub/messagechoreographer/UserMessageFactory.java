package orchowski.tomasz.message_hub.messagechoreographer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
class UserMessageFactory {
    private final ObjectMapper objectMapper;

    UserMessageDto createUserMessage(byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, UserMessageDto.class);
        } catch (IOException e) {
            throw new MessageUnmarshallingException(e);
        }
    }
}
