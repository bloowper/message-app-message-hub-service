package orchowski.tomasz.message_hub.channel;

import java.util.List;

record UserChannelsDto(String userId, List<String> channelsId) {
}
