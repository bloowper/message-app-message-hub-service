package orchowski.tomasz.message_hub.messagechoreographer;


import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.messagechoreographer.api.MessageChoreographerFacade;
import orchowski.tomasz.message_hub.messagechoreographer.api.UserMessageDto;
import orchowski.tomasz.message_hub.shared.TestContainerInfrastructure;
import orchowski.tomasz.message_hub.channel.ChannelInformationServiceStub;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
@ActiveProfiles("messageChoreographer")
class MessageChoreographerServiceBehavioralIT extends TestContainerInfrastructure {
    private static final long MESSAGE_RECEIVE_TIMEOUT = 600;


    @Autowired
    MessageChoreographerFacade messageChoreographerFacade;
    @Autowired
    ChannelInformationServiceStub channelInformationServiceStub;

    @AfterEach
    void afterEach() {
        channelInformationServiceStub.removeAllUsersInformation();// get rid of channels set for all user
    }

    @Test
    void usersShouldReceiveMessageWhenBelongToChannel() {
        // given
        String user1Uuid = "user1Uuid";
        String user2Uuid = "user2Uuid";
        String channelUuid = "channelUuid";
        UserMessageDto messageCreatedByUser2 = new UserMessageDto(Instant.now(), channelUuid, "user2", user2Uuid, "Message content");

        // users1 and user2 participate in same channel
        channelInformationServiceStub.setUserChannels(user1Uuid, List.of(channelUuid)); // m8by mockito would be more readable?
        channelInformationServiceStub.setUserChannels(user2Uuid, List.of(channelUuid));

        // when
        Flux<UserMessageDto> messagesToUser1 = messageChoreographerFacade.getUserMessages(Mono.just(user1Uuid));
        Mono<Void> messageSendByUser2 = messageChoreographerFacade.sendMessage(Mono.just(messageCreatedByUser2));

        // then
        StepVerifier.create(messagesToUser1)
                .then(messageSendByUser2::subscribe)// This line send message after user subscribe
                .expectNextCount(1)
                .as("User should received message")
                .thenCancel().verify();
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
        channelInformationServiceStub.setUserChannels(user1Uuid, List.of(user1ChannelUuid));
        channelInformationServiceStub.setUserChannels(user2Uuid, List.of(user2ChannelUuid));

        // when
        Flux<UserMessageDto> messagesToUser1 = messageChoreographerFacade.getUserMessages(Mono.just(user1Uuid));
        Mono<Void> messageSendByUser2 = messageChoreographerFacade.sendMessage(Mono.just(messageCreatedByUser2));

        // then
        messageSendByUser2.subscribe();

        StepVerifier.create(messagesToUser1)
                .then(messageSendByUser2::subscribe)
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

        channelInformationServiceStub.setUserChannels(userUuid, List.of(channelUuid));

        // when
        Flux<UserMessageDto> messagesToUser = messageChoreographerFacade.getUserMessages(Mono.just(userUuid));
        Mono<Void> messageSendByUser = messageChoreographerFacade.sendMessage(Mono.just(messageCreatedByUser));

        // then

        StepVerifier.create(messagesToUser)
                .then(messageSendByUser::subscribe)
                .expectNextCount(1)
                .as("User should receive message")
                .thenCancel().verify();
    }

    @Test
    void messageShouldBeDeliveredToAllUsersWhenTheyParticipateInSameChannel() {
        // given
        String channelUuid = "channelUuid";
        String user1Uuid = UUID.randomUUID().toString();
        String user2Uuid = UUID.randomUUID().toString();
        String user3Uuid = UUID.randomUUID().toString();

        // all users participate in same channel
        channelInformationServiceStub.setUserChannels(user1Uuid, List.of(channelUuid));
        channelInformationServiceStub.setUserChannels(user2Uuid, List.of(channelUuid));
        channelInformationServiceStub.setUserChannels(user3Uuid, List.of(channelUuid));

        UserMessageDto message = new UserMessageDto(Instant.now(), channelUuid, "user3", user3Uuid, "Message content");

        // when
        Flux<UserMessageDto> messagesToAllUsers = Flux.merge(
                messageChoreographerFacade.getUserMessages(Mono.just(user1Uuid)),
                messageChoreographerFacade.getUserMessages(Mono.just(user2Uuid)),
                messageChoreographerFacade.getUserMessages(Mono.just(user3Uuid))
        );

        Mono<Void> messageToSharedChannel = messageChoreographerFacade.sendMessage(Mono.just(message));

        // then
        StepVerifier.create(messagesToAllUsers)
                .then(messageToSharedChannel::subscribe)
                .assertNext(userMessageDto -> assertEquals(userMessageDto, message))
                .assertNext(userMessageDto -> assertEquals(userMessageDto, message))
                .assertNext(userMessageDto -> assertEquals(userMessageDto, message))
                .thenCancel().verify();
    }

}
