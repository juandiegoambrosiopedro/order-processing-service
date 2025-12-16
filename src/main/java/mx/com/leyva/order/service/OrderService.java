package mx.com.leyva.order.service;

import mx.com.leyva.order.model.OrderRequestDTO;
import mx.com.leyva.order.model.OrderResponseDTO;

/**
 * Interfaz de servicio que define las operaciones relacionadas con el procesamiento de órdenes.
 */
public interface OrderService {

    /** Procesa una orden basada en la información del OrderRequestDTO. */
    OrderResponseDTO processOrder(OrderRequestDTO request);

}
