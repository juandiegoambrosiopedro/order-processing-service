package mx.com.leyva.order.service;

import mx.com.leyva.order.model.InventoryResponseDTO;

/**
 * Interfaz de servicio para consultar el inventario de productos por SKU.
 */
public interface InventoryService {

    /** Obtiene la información de stock de un producto específico identificado por su SKU. */
    InventoryResponseDTO getStockBySku(String sku);

}
