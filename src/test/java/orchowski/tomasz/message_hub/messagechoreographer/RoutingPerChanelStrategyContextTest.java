package orchowski.tomasz.message_hub.messagechoreographer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RoutingPerChanelStrategyContextTest {
    //TODO rewrite tests for importing properties without starting context
    @Autowired
    MessageBrokerProperties messageBrokerProperties;

    @Test
    void shouldReturnRoutingPerChanel() {
        // given
        String serviceUuid = "63d29991-f561-4889-9175-6addc199da42";
        String destinationChanelUuid = "c4e0fbff-3394-4953-b74d-72b336037d3d";

        // when
        String template = messageBrokerProperties.getRoutingKey().getTemplateUserMessage();
        RoutingKeyStrategy routingKeyStrategy = new RoutingPerChanelStrategy(template, destinationChanelUuid, serviceUuid);

        // then
        String routingKey = routingKeyStrategy.getRoutingKey();
        assertEquals("instance.63d29991-f561-4889-9175-6addc199da42.destination-message-chanel.c4e0fbff-3394-4953-b74d-72b336037d3d", routingKey);

    }
}