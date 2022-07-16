package orchowski.tomasz.message_hub.messagehandler;


import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(
        imports = {Instant.class}
)
interface MessageMapper { // TODO provide tests

    @Mapping(target = "creationDate", expression = "java(Instant.now())")
    UserMessageDto toDto(MessageFromUser messageSendByUser);

    MessageToUser fromDto(UserMessageDto userMessageDto);
}
