package orchowski.tomasz.message_hub.channel.api;

import orchowski.tomasz.message_hub.channel.api.ChannelDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChannelInformationFacade {
    Flux<ChannelDto> getUserChannels(Mono<String> userUuidMono);
}
