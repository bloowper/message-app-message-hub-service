package orchowski.tomasz.message_hub.channel;

import orchowski.tomasz.message_hub.channel.dto.ChannelDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChannelInformationFacade {
    Flux<ChannelDto> getUserChannels(Mono<String> userUuidMono);
}
