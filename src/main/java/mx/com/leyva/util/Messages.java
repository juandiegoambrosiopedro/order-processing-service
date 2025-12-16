package mx.com.leyva.util;

import lombok.Data;

/**
 * Clase utilitaria que contiene los mensajes de error y servicio usados en el sistema.
 */
@Data
public final class Messages {

    /** Errores de negocio. */
    public static final String ALREADY_EXISTS = "La orden con ID %s ya existe";

    public static final String NOT_FOUND = "La orden con ID %s no fue encontrada";

    public static final String INSUFFICIENT_STOCK_MESSAGE = "Stock insuficiente para SKU %s";

    /** Errores de validación. */
    public static final String VALIDATION_ERROR = "Ocurrió un error con uno o más datos, por favor valide la información.";

    /** Errores del servidor. */
    public static final String INTERNAL_SERVER_ERROR = "Ocurrió un error interno en el servidor.";

    private Messages() {
        throw new IllegalStateException("Clase de constantes.");
    }

}
