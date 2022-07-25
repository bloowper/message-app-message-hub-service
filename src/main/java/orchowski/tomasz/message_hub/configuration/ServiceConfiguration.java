package orchowski.tomasz.message_hub.configuration;


import orchowski.tomasz.message_hub.configuration.dto.ServiceUuidDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
class ServiceConfiguration {
    @Bean
    ServiceUuidDto serviceUuidDto() {
        return new ServiceUuidDto(UUID.randomUUID().toString());
    }
}
