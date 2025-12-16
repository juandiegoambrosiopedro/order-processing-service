package mx.com.leyva.order.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Configuración de propiedades de la aplicación relacionadas con órdenes.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "order")
public class OrderPropertiesConfig {

    private BigDecimal iva;

    @Bean
    public BigDecimal iva() {
        return iva;
    }

}
