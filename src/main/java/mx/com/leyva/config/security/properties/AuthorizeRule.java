package mx.com.leyva.config.security.properties;

import lombok.Data;

/**
 * Clase que representa una regla de autorización para un endpoint específico.
 */
@Data
public class AuthorizeRule {

    private String method;

    private String path;
}
