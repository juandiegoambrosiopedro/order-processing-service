package mx.com.leyva.config.security.properties;

import lombok.Data;

/* Esta clase contiene la regla de acceso/autenticación para un método y su ruta
 */
@Data
public class PermitAllRule {

    private String path;

}
