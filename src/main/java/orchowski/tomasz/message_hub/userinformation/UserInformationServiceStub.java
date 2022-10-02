package orchowski.tomasz.message_hub.userinformation;

import orchowski.tomasz.message_hub.userinformation.dto.ChannelDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@Profile("!userInformation")
class UserInformationServiceStub implements UserInformationFacade {
    private static final String CHANNEL1 = "99b1ace7-dc61-49a6-8e54-bfafb54f4fac";
    private static final String CHANNEL2 = "86b1555e-cd12-428b-b308-5e843c611b57";
    private static final String CHANNEL3 = "c1b61767-effa-439d-aa97-c0edc47d326e";
    private static final String USER_UUID_1 = "7d7d757b-d9fb-4780-8f04-e784307a2b7a";
    public static final String USER_UUID_2 = "d7b8cfd6-efca-4c3e-9ed3-63d74c80214d";
    public static final String USER_UUID_3 = "022448d7-4a4c-46da-ab67-07b57eff485c";

    private final Map<String, List<String>> userUuidChannelsMap = Map.of(
            USER_UUID_1, List.of(CHANNEL1, CHANNEL2),
            USER_UUID_2, List.of(CHANNEL1, CHANNEL2),
            USER_UUID_3, List.of(CHANNEL1, CHANNEL3)
    );

    @Override
    public Flux<ChannelDto> getUserChannels(Mono<String> userUuidMono) {
        return userUuidMono.map(userUuid -> userUuidChannelsMap.getOrDefault(userUuid, List.of()))
                .flatMapMany(Flux::fromIterable)
                .map(ChannelDto::new);
    }

}
