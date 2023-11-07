package orchowski.tomasz.message_hub.configuration;


import lombok.extern.slf4j.Slf4j;
import orchowski.tomasz.message_hub.configuration.dto.ServiceUuidDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@Slf4j
class ServiceConfiguration {
    /**
     * @return uuid of service instance
     */
    @Bean
    ServiceUuidDto serviceUuidDto() {
        ServiceUuidDto serviceUuidDto = new ServiceUuidDto(UUID.randomUUID().toString());
        log.info("Service instance uuid [{}]", serviceUuidDto.uuid());
        return serviceUuidDto;
    }
}
