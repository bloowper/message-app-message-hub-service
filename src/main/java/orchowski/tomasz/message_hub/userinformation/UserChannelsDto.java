package orchowski.tomasz.message_hub.userinformation;

import java.util.List;

record UserChannelsDto(String userId, List<String> channelsId) {
}
