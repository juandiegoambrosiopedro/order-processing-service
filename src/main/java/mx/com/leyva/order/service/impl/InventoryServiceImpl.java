package mx.com.leyva.order.service.impl;

import lombok.extern.slf4j.Slf4j;
import mx.com.leyva.order.model.InventoryResponseDTO;
import mx.com.leyva.order.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Implementación del servicio InventoryService que gestiona la consulta de stock de productos.
 */
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    /** Mapa simulado de stock de productos, donde la clave es el SKU y el valor la cantidad disponible. */
    private static final Map<String, Integer> STOCK_MOCK = Map.of(
            "SKU-GAS-PLUS-91", 5000,
            "SKU-GAS-REG-87", 8000,
            "SKU-DIESEL", 6000,
            "SKU-UREA-DEF", 2000,
            "SKU-ADIT-LIMP", 300,
            "SKU-ACEITE-5W30", 150
    );

    @Override
    public InventoryResponseDTO getStockBySku(final String sku) {

        int stockDisponible = STOCK_MOCK.getOrDefault(sku, 0);

        log.info("INVENTORY_SERVICE | MOCK_LOOKUP | sku={} | stockDisponible={}", sku, stockDisponible);

        return new InventoryResponseDTO(sku, stockDisponible);
    }
}
