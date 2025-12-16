package mx.com.leyva.order.repository;

import mx.com.leyva.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para gestionar operaciones CRUD sobre la entidad Order.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderId(String orderId);

}
