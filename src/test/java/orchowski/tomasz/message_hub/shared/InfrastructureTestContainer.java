package orchowski.tomasz.message_hub.shared;

import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class InfrastructureTestContainer {
    private static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("3.10.5");

    static {
        rabbitMQContainer.start();
    }

}
