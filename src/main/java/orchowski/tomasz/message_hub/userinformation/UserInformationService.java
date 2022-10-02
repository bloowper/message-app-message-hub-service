package orchowski.tomasz.message_hub.userinformation;

import orchowski.tomasz.message_hub.userinformation.dto.ChannelDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Profile("userInformation")
class UserInformationService implements UserInformationFacade {

    @Override
    public Flux<ChannelDto> getUserChannels(Mono<String> userUuidMono) {
        return null;
    }
}
