package mx.com.leyva.order.exception;

/**
 * Excepción que se lanza cuando no hay suficiente stock disponible para procesar una orden.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(final String message) {
        super(message);
    }
}
