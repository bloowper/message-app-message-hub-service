package orchowski.tomasz.message_hub.channel;

import orchowski.tomasz.message_hub.channel.api.ChannelDto;
import orchowski.tomasz.message_hub.channel.api.ChannelInformationFacade;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile("!channelInformation")
public class ChannelInformationServiceStub implements ChannelInformationFacade {
    private static final String DEFAULT_CHANNEL_UUID = "DEFAULT_CHANNEL_UUID";
    Map<String, List<String>> userChannels = new ConcurrentHashMap<>();


    @Override
    public Flux<ChannelDto> getUserChannels(Mono<String> userUuidMono) {
        assignDefaultChannelToUserIfThereIsNoChannels(userUuidMono);
        return userUuidMono
                .map(userUuid -> Optional.ofNullable(userChannels.get(userUuid)).orElse(List.of()))
                .flatMapMany(Flux::fromIterable)
                .map(ChannelDto::new);
    }

    public void setUserChannels(String userUuid, List<String> chanelUuids) {
        this.userChannels.put(userUuid, chanelUuids);
    }

    public void removeAllUsersInformation() {
        this.userChannels = new ConcurrentHashMap<>();
    }

    private void assignDefaultChannelToUserIfThereIsNoChannels(Mono<String> userUuidMono) {
        userUuidMono.filter(userUuid -> !userChannels.containsKey(userUuid))
                .subscribe(userUuid -> userChannels.put(userUuid, List.of(DEFAULT_CHANNEL_UUID)));
    }
}
