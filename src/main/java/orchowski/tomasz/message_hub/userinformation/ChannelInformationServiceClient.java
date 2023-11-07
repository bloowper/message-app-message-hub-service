package orchowski.tomasz.message_hub.userinformation;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
class ChannelInformationServiceClient {
    private static final String CHANNEL_INFORMATION_SERVICE_BASE_URL = "https://message-app-channel-information-service";
    private final WebClient webClient;

    ChannelInformationServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(CHANNEL_INFORMATION_SERVICE_BASE_URL)
                .build();
    }

    Flux<ChannelInformation> getUserChannels(Mono<String> userIdMono) {
        return userIdMono.flatMapMany(
                userId ->
                        webClient
                                .get()
                                .uri("/api/v1/user/{userId}", Map.of("userId", userId))
                                .retrieve()
                                .bodyToFlux(ChannelInformation.class)
        );
    }
}
