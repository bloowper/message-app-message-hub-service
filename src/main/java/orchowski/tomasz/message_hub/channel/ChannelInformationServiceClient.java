package orchowski.tomasz.message_hub.channel;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                                .uri(uriBuilder -> uriBuilder
                                        .path("/api/v1/channels")
                                        .queryParam("userId",userId)
                                        .build()
                                )
                                .retrieve()
                                .bodyToFlux(ChannelInformation.class)
        );
    }
}
