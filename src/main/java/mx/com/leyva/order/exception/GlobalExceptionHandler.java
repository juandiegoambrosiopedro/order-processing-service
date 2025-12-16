package mx.com.leyva.order.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import mx.com.leyva.util.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Clase para manejo global de excepciones para los controladores REST, centralizando la gestión de errores y respuestas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Respuesta HTTP 400 BAD_REQUEST con los detalles del error. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> badRequest(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errors.isEmpty() ? Messages.VALIDATION_ERROR : errors,
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }

    /** Respuesta HTTP 400 BAD_REQUEST con los detalles de errores de conversión (enum inválido, tipos incorrectos). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = Messages.VALIDATION_ERROR;

        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) ex.getCause();
            String fieldName = cause.getPath().get(0).getFieldName();
            message = "El campo '" + fieldName + "' tiene un valor inválido: '" + cause.getValue() + "'";
        }
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }

    /** Respuesta HTTP 409 CONFLICT con los detalles del error, cuando se intenta crear una orden que ya existe. */
    @ExceptionHandler(OrderAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> conflict(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    /** Respuesta HTTP 409 CONFLICT cuando inventario es insuficiente. */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /** Respuesta HTTP 409 CONFLICT cuando ocurre un error interno en el servidor. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> serverError() {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Messages.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
