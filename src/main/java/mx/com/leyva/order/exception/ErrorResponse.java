package mx.com.leyva.order.exception;

import java.time.LocalDateTime;

/**
 * Representa la estructura de respuesta en caso de error en las operaciones del sistema.
 */
public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp
) {}
