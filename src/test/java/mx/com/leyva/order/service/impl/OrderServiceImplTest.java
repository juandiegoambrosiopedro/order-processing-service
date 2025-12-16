package mx.com.leyva.order.service.impl;

import mx.com.leyva.order.config.OrderPropertiesConfig;
import mx.com.leyva.order.entity.Order;
import mx.com.leyva.order.enums.Canal;
import mx.com.leyva.order.exception.InsufficientStockException;
import mx.com.leyva.order.exception.OrderAlreadyExistsException;
import mx.com.leyva.order.model.InventoryResponseDTO;
import mx.com.leyva.order.model.OrderItemRequestDTO;
import mx.com.leyva.order.model.OrderRequestDTO;
import mx.com.leyva.order.model.OrderResponseDTO;
import mx.com.leyva.order.repository.OrderRepository;
import mx.com.leyva.order.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderItemRequestDTO buildItem(String sku, int cantidad, double precioUnitario) {
        OrderItemRequestDTO item = new OrderItemRequestDTO();
        item.setSku(sku);
        item.setCantidad(cantidad);
        item.setPrecioUnitario(BigDecimal.valueOf(precioUnitario));
        return item;
    }

    private OrderRequestDTO buildRequest(String orderId, String customerId, Canal canal, List<OrderItemRequestDTO> items) {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setOrderId(orderId);
        request.setCustomerId(customerId);
        request.setCanal(canal);
        request.setItems(items);
        return request;
    }

    @BeforeEach
    void setup() {
        // Creamos OrderProperties de prueba
        OrderPropertiesConfig orderProperties = new OrderPropertiesConfig();
        orderProperties.setIva(new BigDecimal("0.16"));

        // Inyectamos manualmente en orderService
        ReflectionTestUtils.setField(orderService, "orderProperties", orderProperties);
    }

    @Test
    void processOrder_when_riesgoBajo() {

        OrderRequestDTO request = buildRequest("ORD-FUEL-LOW-0003", "CUST-PARTICULAR-01", Canal.WEB, List.of(buildItem("SKU-ADIT-LIMP", 1, 350.00)));

        Mockito.when(orderRepository.existsByOrderId(request.getOrderId()))
                .thenReturn(false);

        Mockito.when(inventoryService.getStockBySku("SKU-ADIT-LIMP"))
                .thenReturn(new InventoryResponseDTO("SKU-ADIT-LIMP", 300));

        OrderResponseDTO response = orderService.processOrder(request);

        assertNotNull(response);
        assertEquals("ORD-FUEL-LOW-0003", response.getOrderId());
        assertEquals("350.00", response.getSubtotal());
        assertEquals("56.00", response.getIva());
        assertEquals("406.00", response.getTotal());
        assertEquals("BAJO", response.getRiesgo());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void processOrder_when_riesgoMedio() {

        OrderRequestDTO request = buildRequest("ORD-FUEL-0002", "CUST-FLOTILLA-02", Canal.APP, List.of(
                        buildItem("SKU-DIESEL", 100, 24.80),
                        buildItem("SKU-UREA-DEF", 20, 18.00)
                )
        );

        Mockito.when(orderRepository.existsByOrderId(request.getOrderId()))
                .thenReturn(false);

        Mockito.when(inventoryService.getStockBySku("SKU-DIESEL"))
                .thenReturn(new InventoryResponseDTO("SKU-DIESEL", 100));

        Mockito.when(inventoryService.getStockBySku("SKU-UREA-DEF"))
                .thenReturn(new InventoryResponseDTO("SKU-UREA-DEF", 20));

        OrderResponseDTO response = orderService.processOrder(request);

        assertNotNull(response);
        assertEquals("ORD-FUEL-0002", response.getOrderId());
        assertEquals("2,840.00", response.getSubtotal());
        assertEquals("454.40", response.getIva());
        assertEquals("3,294.40", response.getTotal());
        assertEquals("MEDIO", response.getRiesgo());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void processOrder_when_riesgoAlto() {

        OrderRequestDTO request = buildRequest("ORD-FUEL-0003", "CUST-TALLER-03", Canal.CALLCENTER, List.of(
                        buildItem("SKU-ACEITE-5W30", 20, 450.00),
                        buildItem("SKU-ADIT-LIMP", 10, 350.00)
                )
        );

        Mockito.when(orderRepository.existsByOrderId(request.getOrderId()))
                .thenReturn(false);

        Mockito.when(inventoryService.getStockBySku("SKU-ACEITE-5W30"))
                .thenReturn(new InventoryResponseDTO("SKU-ACEITE-5W30", 20));

        Mockito.when(inventoryService.getStockBySku("SKU-ADIT-LIMP"))
                .thenReturn(new InventoryResponseDTO("SKU-ADIT-LIMP", 10));

        OrderResponseDTO response = orderService.processOrder(request);

        assertNotNull(response);
        assertEquals("ORD-FUEL-0003", response.getOrderId());
        assertEquals("12,500.00", response.getSubtotal());
        assertEquals("2,000.00", response.getIva());
        assertEquals("14,500.00", response.getTotal());
        assertEquals("ALTO", response.getRiesgo());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void processOrder_whenOrder_alreadyExists_409() {

        OrderRequestDTO request = buildRequest("ORD-FUEL-0005", "CUST-EXISTENTE", Canal.WEB, List.of(
                buildItem("SKU-ADIT-LIMP", 1, 350.00))
        );

        Mockito.when(orderRepository.existsByOrderId(request.getOrderId()))
                .thenReturn(true);

        OrderAlreadyExistsException ex = assertThrows(OrderAlreadyExistsException.class, () -> orderService.processOrder(request));

        assertTrue(ex.getMessage().contains(request.getOrderId()));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void processOrder_whenStock_isInsufficient_409() {

        OrderRequestDTO request = buildRequest("ORD-FUEL-0004", "CUST-ACEITE", Canal.WEB, List.of(
                buildItem("SKU-ACEITE-5W30", 200, 450.00))
        );

        Mockito.when(orderRepository.existsByOrderId(anyString()))
                .thenReturn(false);

        Mockito.when(inventoryService.getStockBySku("SKU-ACEITE-5W30"))
                .thenReturn(new InventoryResponseDTO("SKU-ACEITE-5W30", 1));

        InsufficientStockException ex = assertThrows(
                InsufficientStockException.class,
                () -> orderService.processOrder(request)
        );

        assertTrue(ex.getMessage().contains("SKU-ACEITE-5W30"));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void processOrder_whenUnexpectedError_500() {
        OrderRequestDTO request = buildRequest(
                "ORD-GAS-5002",
                "CUST-QUE-ES-MUY-LARGO-Y-EXCEDE-EL-TAMAÑO-MAXIMO-PERMITIDO-EN-LA-BASE-DE-DATOS",
                Canal.WEB,
                List.of(buildItem("SKU-DIESEL", 1, 20.50))
        );

        Mockito.when(orderRepository.existsByOrderId(request.getOrderId()))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> orderService.processOrder(request));

        verify(orderRepository, never()).save(any());
    }


}
