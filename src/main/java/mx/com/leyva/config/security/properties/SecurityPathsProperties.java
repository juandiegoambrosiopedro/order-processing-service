package mx.com.leyva.config.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Esta clase sirve para leer la propiedad del archivo application.yml
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security.requests")
public class SecurityPathsProperties {

    /** Lista de objetos PermitAllRule el tipo de método y la URL. */
    private List<PermitAllRule> ignoring;

    private List<AuthorizeRule> authorize;

    private String defaultAccess = "denyAll";

}
