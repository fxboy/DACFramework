package icu.weboys.dacf.services.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Data
@Primary
@ConfigurationProperties(prefix = "dacf.services")
@Configuration
@Qualifier("dacfConfig")
public class DACFConfig {
    Boolean socketServer = false;
    Integer serverPort = 9099;
    String mode = "stand-alone";
}
