package group12.ecwms.moonpham.features.orders.repository;

import group12.ecwms.moonpham.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderCode(String orderCode);
    List<Order> findByUser_IdOrderByCreatedAtDesc(Long userId);
    Optional<Order> findByOrderCodeAndUser_Id(String orderCode, Long userId);
}

