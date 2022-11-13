package orchowski.tomasz.message_hub.userinformation;

import orchowski.tomasz.message_hub.userinformation.dto.ChannelDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile("!userInformation")
public class UserInformationServiceStub implements UserInformationFacade {

    Map<String, List<String>> userChannels = new ConcurrentHashMap<>();


    @Override
    public Flux<ChannelDto> getUserChannels(Mono<String> userUuidMono) {
        return userUuidMono
                .map(userUuid -> Optional.ofNullable(userChannels.get(userUuid)).orElse(List.of()))
                .flatMapMany(Flux::fromIterable)
                .map(ChannelDto::new);
    }

    public void setUserChannels(String userUuid, List<String> chanelUuids) {
        this.userChannels.put(userUuid, chanelUuids);
    }

    public void removeAllUsersInformation() {
        this.userChannels.keySet().removeIf(s -> true);
    }
}
