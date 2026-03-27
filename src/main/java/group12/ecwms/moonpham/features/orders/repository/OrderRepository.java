package group12.ecwms.moonpham.features.auth.repository;

import group12.ecwms.moonpham.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderCode(String orderCode);
    List<Order> findByUser_IdOrderByCreatedAtDesc(Long userId);
    Optional<Order> findByOrderCodeAndUser_Id(String orderCode, Long userId);
}

