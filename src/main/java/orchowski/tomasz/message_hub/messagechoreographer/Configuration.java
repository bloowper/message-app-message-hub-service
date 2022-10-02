package orchowski.tomasz.message_hub.messagechoreographer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@org.springframework.context.annotation.Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({MessageBrokerProperties.class})
class Configuration {

}
