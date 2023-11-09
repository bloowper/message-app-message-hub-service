package orchowski.tomasz.message_hub.channel;

import lombok.RequiredArgsConstructor;
import orchowski.tomasz.message_hub.channel.dto.ChannelDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Profile("userInformation")
@RequiredArgsConstructor
class ChannelInformationService implements ChannelInformationFacade {

    private final ChannelInformationServiceClient channelInformationServiceClient;

    @Override
    public Flux<ChannelDto> getUserChannels(Mono<String> userUuidMono) {
        return channelInformationServiceClient.getUserChannels(userUuidMono)
                .map(channelInformation -> new ChannelDto(channelInformation.id()));
    }

}
