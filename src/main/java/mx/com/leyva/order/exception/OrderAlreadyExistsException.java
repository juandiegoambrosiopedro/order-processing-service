package mx.com.leyva.order.exception;

/**
 * Excepción que se lanza cuando se intenta crear una orden que ya existe.
 */
public class OrderAlreadyExistsException extends RuntimeException {

    public OrderAlreadyExistsException(final String message) {
        super(message);
    }
}
