package group12.ecwms.moonpham.features.orders.repository;

import group12.ecwms.moonpham.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder_OrderCode(String orderCode);
}

