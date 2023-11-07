package orchowski.tomasz.message_hub.shared;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
public class TestContainerInfrastructure {
    private static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.10.5");

    static {
        rabbitMQContainer.start();
    }


    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
        registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
        registry.add("spring.cloud.discovery.enabled", () -> false);// eureka just mess up with logs
    }
}
