package orchowski.tomasz.message_hub.messagechoreographer.adapters.common;


import orchowski.tomasz.message_hub.messagechoreographer.api.UserMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(
        imports = {Instant.class}
)
public interface MessageMapper { // TODO provide tests

    @Mapping(target = "creationDate", expression = "java(Instant.now())") // TODO get rid of this, pass as argument
    @Mapping(target = "userUuid")
    UserMessageDto toDto(MessageFromUser messageSendByUser, String userUuid);

    MessageToUser fromDto(UserMessageDto userMessageDto);
}
