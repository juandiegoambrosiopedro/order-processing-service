package mx.com.leyva.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.leyva.order.config.OrderPropertiesConfig;
import mx.com.leyva.order.entity.Order;
import mx.com.leyva.order.entity.OrderItem;
import mx.com.leyva.order.enums.Canal;
import mx.com.leyva.order.exception.InsufficientStockException;
import mx.com.leyva.order.exception.OrderAlreadyExistsException;
import mx.com.leyva.order.model.InventoryResponseDTO;
import mx.com.leyva.order.model.OrderItemRequestDTO;
import mx.com.leyva.order.model.OrderRequestDTO;
import mx.com.leyva.order.model.OrderResponseDTO;
import mx.com.leyva.order.repository.OrderRepository;
import mx.com.leyva.order.service.InventoryService;
import mx.com.leyva.order.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import static mx.com.leyva.util.Messages.ALREADY_EXISTS;
import static mx.com.leyva.util.Messages.INSUFFICIENT_STOCK_MESSAGE;

/**
 * Implementación del servicio OrderService que gestiona el procesamiento de órdenes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    /** Formato de moneda en pesos mexicanos con decimales. */
    private static final String FORMATO_MONEDA_MX_DECIMAL = "#,##0.00";

    /** Repositorio de órdenes. */
    private final OrderRepository orderRepository;

    /** Servicio de inventario. */
    private final InventoryService inventoryService;

    /** Configuración de propiedades de órdenes. */
    private final OrderPropertiesConfig orderProperties;

    /** Formateador de números usando el formato de moneda MXP. */
    DecimalFormat decimalFormatMXP = new DecimalFormat(FORMATO_MONEDA_MX_DECIMAL);

    @Override
    @Transactional
    public OrderResponseDTO processOrder(final OrderRequestDTO request) {
        OrderResponseDTO response = new OrderResponseDTO();

        if (orderRepository.existsByOrderId(request.getOrderId())) {
            throw new OrderAlreadyExistsException(String.format(ALREADY_EXISTS, request.getOrderId()));
        }
        // Valida el inventario
        validateInventory(request);

        // Cálculos
        BigDecimal subtotal = calculateSubtotal(request.getItems());
        BigDecimal iva      = calculateIva(subtotal);
        BigDecimal total    = subtotal.add(iva);

        // Cálculo de riesgo
        String riesgo = calculateOrderRisk(request.getCanal(), total);

        Order order = new Order();
        order.setOrderId(request.getOrderId());
        order.setCustomerId(request.getCustomerId());
        order.setCanal(request.getCanal());
        order.setSubtotal(subtotal);
        order.setIva(iva);
        order.setTotal(total);
        order.setRiesgo(riesgo);

        // Crea una lista de ordenItems
        List<OrderItem> items = createOrderItems(request.getItems(), order);
        order.setItems(items);

        orderRepository.save(order);

        response.setOrderId(order.getOrderId());
        response.setSubtotal(decimalFormatMXP.format(order.getSubtotal()));
        response.setIva(decimalFormatMXP.format(order.getIva()));
        response.setTotal(decimalFormatMXP.format(order.getTotal()));
        response.setRiesgo(order.getRiesgo());

        return response;
    }

    private void validateInventory(final OrderRequestDTO request) {
        for (OrderItemRequestDTO item : request.getItems()) {

            InventoryResponseDTO inventory = inventoryService.getStockBySku(item.getSku());

            log.info("INVENTORY_VALIDATION | sku={} | requested={} | available={}", item.getSku(), item.getCantidad(), inventory.getStockDisponible());

            if (inventory.getStockDisponible() < item.getCantidad()) {
                log.info("Stock insuficiente para SKU {}", item.getSku());
                throw new InsufficientStockException(String.format(INSUFFICIENT_STOCK_MESSAGE, item.getSku()));
            }

        }
    }

    private String calculateOrderRisk(final Canal canal, final BigDecimal total) {
        if (canal == Canal.CALLCENTER && total.compareTo(BigDecimal.valueOf(5000)) > 0) {
            return "ALTO";
        }

        if (canal == Canal.WEB && total.compareTo(BigDecimal.valueOf(1000)) < 0) {
            return "BAJO";
        }

        return "MEDIO";
    }

    private BigDecimal calculateSubtotal(final List<OrderItemRequestDTO> items) {
        return items.stream()
                .map(this::calculateItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateItemTotal(final OrderItemRequestDTO item) {
        return item.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(item.getCantidad()));
    }

    private BigDecimal calculateIva(final BigDecimal subtotal) {
        return subtotal.multiply(orderProperties.getIva());
    }

    private List<OrderItem> createOrderItems(final List<OrderItemRequestDTO> itemDTOs, final Order order) {
        return itemDTOs.stream()
                .map(dto -> {
                    OrderItem item = new OrderItem();
                    item.setSku(dto.getSku());
                    item.setCantidad(dto.getCantidad());
                    item.setPrecioUnitario(dto.getPrecioUnitario());
                    item.setOrder(order);
                    return item;
                })
                .toList();
    }

}
