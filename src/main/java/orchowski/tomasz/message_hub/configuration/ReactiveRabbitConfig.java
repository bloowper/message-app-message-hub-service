package orchowski.tomasz.message_hub.configuration;


import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

@Configuration
@Slf4j
class ReactiveRabbitConfig {

    @Bean
    Mono<Connection> connectionMono(@Autowired org.springframework.amqp.rabbit.connection.ConnectionFactory springAmqpConnectionFactory) {
        ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();
        connectionFactory.useNio();
        connectionFactory.setPort(springAmqpConnectionFactory.getPort());
        connectionFactory.setHost(springAmqpConnectionFactory.getHost());
        return Mono.fromCallable(connectionFactory::newConnection);
    }

    @Bean
    Receiver receiverRabbitFlux(Mono<Connection> connectionMono) {
        ReceiverOptions receiverOptions = new ReceiverOptions().connectionMono(connectionMono);
        return RabbitFlux.createReceiver(receiverOptions);
    }

    @Bean
    Sender senderRabbitFlux(Mono<Connection> connectionMono) {
        SenderOptions senderOptions = new SenderOptions().connectionMono(connectionMono).resourceManagementScheduler(Schedulers.boundedElastic());
        return RabbitFlux.createSender(senderOptions);
    }

}
