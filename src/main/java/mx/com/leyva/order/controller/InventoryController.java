package mx.com.leyva.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.leyva.order.model.InventoryResponseDTO;
import mx.com.leyva.order.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory", description = "API para la consulta de inventario y disponibilidad de stock por producto.")
public class InventoryController {

    /** Servicio de inventario utilizado para consultar la disponibilidad de stock de productos. */
    private final InventoryService inventoryService;

    @Operation(summary = "Consulta de inventario y disponibilidad de stock por producto.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/sku/{sku}")
    public ResponseEntity<InventoryResponseDTO> getStock(final @PathVariable String sku) {
        log.info("________________.: START | INVENTORY_API | GET_STOCK | sku={}", sku);

        InventoryResponseDTO response = inventoryService.getStockBySku(sku);

        log.info("________________.: END   | INVENTORY_API | GET_STOCK | sku={}", sku);
        return ResponseEntity.ok(response);
    }
}
