package orchowski.tomasz.message_hub.messagechoreographer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoutingPerChanelStrategyTest {

    @Test
    void shouldReturnRoutingKey() {
        // given
        var routingTemplate = "instance.%s.destination-message-chanel.%s";
        var destinationChanelUuid = "b4a8e8dc-14ef-4867-abc0-a842110e4c20";
        var serviceUuid = "d555908d-5b36-40d0-96d8-324e9d30c26a";

        // when
        RoutingKeyStrategy routingKeyStrategy = new RoutingPerChanelStrategy(routingTemplate, destinationChanelUuid, serviceUuid);

        // then
        System.out.println(routingKeyStrategy.getRoutingKey());
        assertEquals("instance.d555908d-5b36-40d0-96d8-324e9d30c26a.destination-message-chanel.b4a8e8dc-14ef-4867-abc0-a842110e4c20",routingKeyStrategy.getRoutingKey());
    }
}