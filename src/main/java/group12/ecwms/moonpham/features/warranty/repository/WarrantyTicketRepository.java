package group12.ecwms.moonpham.features.warranty.repository;

import group12.ecwms.moonpham.domain.entity.WarrantyTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyTicketRepository extends JpaRepository<WarrantyTicket, Long> {

    List<WarrantyTicket> findByDigitalWarranty_IdOrderByCreatedAtDesc(Long digitalWarrantyId);
}
