package orchowski.tomasz.message_hub.userinformation;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class REMOVE_ME implements CommandLineRunner {

    private final ChannelInformationServiceClient channelInformationServiceClient;

    @Override
    public void run(String... args) throws Exception {
        Flux<UserChannelsDto> userChannels = channelInformationServiceClient.getUserChannels(Mono.just("fe1137a0-6736-4868-ad58-aefcdf800509"));
        userChannels.subscribe(userChannelsDto -> {
            System.out.println(userChannelsDto);
        });
    }
}
