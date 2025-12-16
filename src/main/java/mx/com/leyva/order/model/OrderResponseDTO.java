package mx.com.leyva.order.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta con los datos de la orden procesada.")
public class OrderResponseDTO {

    @Schema(description = "Identificador único de la orden", example = "ORD-GAS-0001")
    private String orderId;

    @Schema(description = "Subtotal de la orden", example = "102,500.00")
    private String subtotal;

    @Schema(description = "IVA calculado (16%)", example = "16,400.00")
    private String iva;

    @Schema(description = "Total final de la orden", example = "118,900.00")
    private String total;

    @Schema(description = "Nivel de riesgo calculado", example = "MEDIO")
    private String riesgo;

}
