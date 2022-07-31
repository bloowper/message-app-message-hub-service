package orchowski.tomasz.message_hub.userinformation;

import orchowski.tomasz.message_hub.userinformation.dto.ChannelDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserInformationFacade {
    Flux<ChannelDto> getUserChannels(Mono<String> userUuidMono);
}
