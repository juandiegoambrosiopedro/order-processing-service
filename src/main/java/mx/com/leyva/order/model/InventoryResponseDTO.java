package mx.com.leyva.order.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta que contiene la disponibilidad de inventario para un producto identificado por SKU.")
public class InventoryResponseDTO {

    @Schema(description = "Código SKU del producto", example = "SKU-GAS-PLUS-91")
    private String sku;

    @Schema(description = "Cantidad disponible en inventario", example = "5000")
    private int stockDisponible;

}
