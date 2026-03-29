package group12.ecwms.moonpham.features.warranty.services;

import group12.ecwms.moonpham.domain.entity.DigitalWarranty;
import group12.ecwms.moonpham.domain.entity.WarrantyTicket;

import java.util.List;
import java.util.Optional;

public interface WarrantyService {

    List<DigitalWarranty> listForUser(Long userId, String searchQuery);

    Optional<DigitalWarranty> findDetailForUser(Long userId, Long warrantyId);

    List<WarrantyTicket> listTickets(Long warrantyId);

    boolean isWarrantyExpired(DigitalWarranty warranty);

    /**
     * @return null on success, otherwise an error message for the user (Thymeleaf flow).
     */
    String createSupportTicket(Long userId, Long warrantyId, String issueType, String description);
}
