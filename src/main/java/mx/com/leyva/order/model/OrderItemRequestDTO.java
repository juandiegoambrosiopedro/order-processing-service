package mx.com.leyva.order.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Schema(description = "Item individual de una orden.")
public class OrderItemRequestDTO {

    @Schema(description = "Código SKU del producto", example = "SKU-GAS-PLUS-91")
    @NotBlank
    private String sku;

    @Schema(description = "Cantidad del producto", example = "5000")
    @NotNull
    @Min(value = 1, message = "La cantidad debe ser mayor a cero.")
    private Integer cantidad;

    @Schema(description = "Precio unitario", example = "20.50")
    @NotNull
    @DecimalMin(value = "0.01", message = "El precio unitario deber ser mayor que cero.")
    private BigDecimal precioUnitario;

}
