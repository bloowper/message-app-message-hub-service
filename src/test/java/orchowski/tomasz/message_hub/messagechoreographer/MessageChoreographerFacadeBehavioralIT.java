package orchowski.tomasz.message_hub.messagechoreographer;


import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagehandler.dto.UserMessageDto;
import orchowski.tomasz.message_hub.shared.TestContainerInfrastructure;
import orchowski.tomasz.message_hub.userinformation.UserInformationServiceStub;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = {MessageChoreographerFacadeBehavioralIT.MessageChoreographerConfiguration.class})
@Slf4j
class MessageChoreographerFacadeBehavioralIT extends TestContainerInfrastructure {
    private static final long MESSAGE_SEND_DELAY_MILLIS = 200;
    private static final long MESSAGE_RECEIVE_TIMEOUT = MESSAGE_SEND_DELAY_MILLIS + 200;


    @Autowired
    MessageChoreographerFacade messageChoreographerFacade;
    @Autowired
    UserInformationServiceStub userInformationServiceStub;

    @AfterEach
    void afterEach() {
        userInformationServiceStub.removeAllUsersInformation();// get rid of channels set for all user
    }

    @Test
    void usersShouldReceiveMessageWhenBelongToChannel() {
        // given
        String user1Uuid = "user1Uuid";
        String user2Uuid = "user2Uuid";
        String channelUuid = "channelUuid";
        UserMessageDto messageCreatedByUser2 = new UserMessageDto(Instant.now(), channelUuid, "user2", user2Uuid, "Message content");

        // users1 and user2 participate in same channel
        userInformationServiceStub.setUserChannels(user1Uuid, List.of(channelUuid));
        userInformationServiceStub.setUserChannels(user2Uuid, List.of(channelUuid));

        // when
        Flux<UserMessageDto> messagesToUser1 = messageChoreographerFacade.getUserMessages(Mono.just(user1Uuid));
        Mono<Void> messageSendByUser2 = messageChoreographerFacade.sendMessage(
                        Mono.just(messageCreatedByUser2)
                                .delayElement(Duration.ofMillis(MESSAGE_SEND_DELAY_MILLIS))
                                .doOnNext(userMessageDto -> log.info("Publishing message: {}", userMessageDto))
                )
                .subscribeOn(Schedulers.parallel());

        // then
        messageSendByUser2.subscribe();

        StepVerifier.create(messagesToUser1)
                .expectNextCount(1)
                .as("User should received message")
                .verifyTimeout(Duration.ofMillis(MESSAGE_RECEIVE_TIMEOUT));
    }

    @Test
    void userShouldNotReceiveMessageWhenNotBelongToChannel() {
        // given
        String user1Uuid = "user1Uuid";
        String user1ChannelUuid = "user1ChannelUuid";

        String user2Uuid = "user2Uuid";
        String user2 = "user2";
        String user2ChannelUuid = "channelWithoutUser1Uuid";

        UserMessageDto messageCreatedByUser2 = new UserMessageDto(Instant.now(), user2ChannelUuid, user2, user2Uuid, "This message should not be received by user1");

        // user1 and user2 NOT SHARING ANY CHANNELS
        userInformationServiceStub.setUserChannels(user1Uuid, List.of(user1ChannelUuid));
        userInformationServiceStub.setUserChannels(user2Uuid, List.of(user2ChannelUuid));

        // when
        Flux<UserMessageDto> messagesToUser1 = messageChoreographerFacade.getUserMessages(Mono.just(user1Uuid));
        Mono<Void> messageSendByUser2 = messageChoreographerFacade.sendMessage(
                        Mono.just(messageCreatedByUser2)
                                .delayElement(Duration.ofMillis(MESSAGE_SEND_DELAY_MILLIS))
                                .doOnNext(userMessageDto -> log.info("Publishing message: {}", userMessageDto))
                )
                .subscribeOn(Schedulers.parallel());

        // then
        messageSendByUser2.subscribe();

        StepVerifier.create(messagesToUser1)
                .expectNextCount(0)
                .as("User should not receive message")
                .verifyTimeout(Duration.ofMillis(MESSAGE_RECEIVE_TIMEOUT));

    }

    @Test
    void userShouldGetHisOwnMessage() {
        // given
        String userUuid = "userUuid";
        String channelUuid = "channelUuid";
        UserMessageDto messageCreatedByUser = new UserMessageDto(Instant.now(), channelUuid, "username", userUuid, "This is message send by user1");

        userInformationServiceStub.setUserChannels(userUuid, List.of(channelUuid));

        // when
        Flux<UserMessageDto> messagesToUser = messageChoreographerFacade.getUserMessages(Mono.just(userUuid));
        Mono<Void> messageSendByUser = messageChoreographerFacade.sendMessage(
                Mono.just(messageCreatedByUser)
                        .delayElement(Duration.ofMillis(MESSAGE_SEND_DELAY_MILLIS))
                        .doOnNext(userMessageDto -> log.info("Publishing message: {}", userMessageDto))
        ).subscribeOn(Schedulers.parallel());

        // then
        messageSendByUser.subscribe();

        StepVerifier.create(messagesToUser)
                .expectNextCount(1)
                .as("User should receive message")
                .verifyTimeout(Duration.ofMillis(MESSAGE_RECEIVE_TIMEOUT));
    }

    @Test
    void messageShouldBeDeliveredToAllUsersWhenTheyParticipateInSameChannel() {
        // given
        String channelUuid = "channelUuid";
        String user1Uuid = UUID.randomUUID().toString();
        String user2Uuid = UUID.randomUUID().toString();
        String user3Uuid = UUID.randomUUID().toString();

        // all users participate in same channel
        userInformationServiceStub.setUserChannels(user1Uuid, List.of(channelUuid));
        userInformationServiceStub.setUserChannels(user2Uuid, List.of(channelUuid));
        userInformationServiceStub.setUserChannels(user3Uuid, List.of(channelUuid));

        UserMessageDto message = new UserMessageDto(Instant.now(), channelUuid, "user3", user3Uuid, "Message content");

        // when
        Flux<UserMessageDto> messagesToAllUsers = Flux.merge(
                messageChoreographerFacade.getUserMessages(Mono.just(user1Uuid)),
                messageChoreographerFacade.getUserMessages(Mono.just(user2Uuid)),
                messageChoreographerFacade.getUserMessages(Mono.just(user3Uuid))
        );

        Mono<Void> messageSendByUser3 = messageChoreographerFacade.sendMessage(
                Mono.just(message)
                        .delayElement(Duration.ofMillis(MESSAGE_SEND_DELAY_MILLIS))
                        .doOnNext(userMessageDto -> log.info("Publishing message: {}", userMessageDto))
        ).subscribeOn(Schedulers.parallel());

        // then
        messageSendByUser3.subscribe();

        StepVerifier.create(messagesToAllUsers)
                .assertNext(userMessageDto -> assertEquals(userMessageDto, message))
                .assertNext(userMessageDto -> assertEquals(userMessageDto, message))
                .assertNext(userMessageDto -> assertEquals(userMessageDto, message))
                .verifyTimeout(Duration.ofMillis(MESSAGE_RECEIVE_TIMEOUT));
    }

    @TestConfiguration
    static class MessageChoreographerConfiguration {
        // TODO resolve problem with spring profiles that makes hard to start test with @Profile("!userInformation") ( start context with stub instead of real implementation)
        @Bean
        @Primary
        UserInformationServiceStub userInformationServiceStub() {
            return new UserInformationServiceStub();
        }
    }

}
