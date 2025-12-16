package mx.com.leyva.order.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.com.leyva.order.enums.Canal;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Order - Representa una orden del sistema y mapea la entidad de base de datos "ORDERS".
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "ORDER_ID", length = 50, nullable = false, unique = true)
    private String orderId;

    @Column(name = "CUSTOMER_ID", length = 50, nullable = false)
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "CANAL", length = 20, nullable = false)
    private Canal canal;

    @Column(name = "SUBTOTAL", precision = 12, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(name = "IVA", precision = 12, scale = 2, nullable = false)
    private BigDecimal iva;

    @Column(name = "TOTAL", precision = 12, scale = 2, nullable = false)
    private BigDecimal total;

    @Column(name = "RIESGO", length = 10, nullable = false)
    private String riesgo;

    @Column(name = "CREATED_AT", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();
}
