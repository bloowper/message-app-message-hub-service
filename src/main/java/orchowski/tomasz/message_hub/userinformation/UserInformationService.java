package orchowski.tomasz.message_hub.userinformation;

import lombok.RequiredArgsConstructor;
import orchowski.tomasz.message_hub.userinformation.dto.ChannelDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Profile("userInformation")
@RequiredArgsConstructor
class UserInformationService implements UserInformationFacade {

    private final ChannelInformationServiceClient channelInformationServiceClient;

    @Override
    public Flux<ChannelDto> getUserChannels(Mono<String> userUuidMono) {
        return channelInformationServiceClient.getUserChannels(userUuidMono)
                .map(channelInformation -> new ChannelDto(channelInformation.id()));
    }

    private Flux<ChannelDto> flatUserChannels(UserChannelsDto userChannelsDto) {
        return Flux.fromStream(
                userChannelsDto.channelsId().stream().map(ChannelDto::new)
        );
    }
}
