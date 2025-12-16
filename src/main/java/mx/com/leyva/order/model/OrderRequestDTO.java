package mx.com.leyva.order.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.com.leyva.order.enums.Canal;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Datos de solicitud para procesar una nueva orden.")
public class OrderRequestDTO {

    @Schema(description = "Identificador único de la orden", example = "ORD-GAS-0001")
    @NotBlank
    private String orderId;

    @Schema(description = "Identificador del cliente", example = "CUST-GAS-01")
    @NotBlank
    private String customerId;

    @Schema(description = "Canal de origen", example = "WEB")
    @NotNull
    private Canal canal;

    @Schema(description = "Lista de productos de la orden")
    @NotEmpty
    @Valid
    private List<OrderItemRequestDTO> items;

}
