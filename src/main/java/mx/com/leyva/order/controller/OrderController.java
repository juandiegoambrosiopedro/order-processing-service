package mx.com.leyva.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.leyva.order.model.OrderRequestDTO;
import mx.com.leyva.order.model.OrderResponseDTO;
import mx.com.leyva.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "API para gestión de órdenes")
public class OrderController {

    /** Servicio de órdenes utilizado para procesar las órdenes de los clientes. */
    private final OrderService orderService;

    @Operation(summary = "Procesar una nueva orden")
    @ApiResponse(responseCode = "201", description = "Orden procesada exitosamente")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/process")
    public ResponseEntity<OrderResponseDTO> process(final @Valid @RequestBody OrderRequestDTO request) {
        log.info("________________.: START | ORDER_API | PROCESS_ORDER | orderId={} | canal={}", request.getOrderId(), request.getCanal());

        OrderResponseDTO response = orderService.processOrder(request);

        log.info("________________.: END   | ORDER_API | PROCESS_ORDER | orderId={}", request.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
