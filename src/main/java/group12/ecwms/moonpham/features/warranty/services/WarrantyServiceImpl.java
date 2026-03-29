package group12.ecwms.moonpham.features.warranty.services;

import group12.ecwms.moonpham.domain.entity.DigitalWarranty;
import group12.ecwms.moonpham.domain.entity.WarrantyTicket;
import group12.ecwms.moonpham.domain.enums.TicketStatus;
import group12.ecwms.moonpham.domain.enums.WarrantyStatus;
import group12.ecwms.moonpham.features.warranty.repository.DigitalWarrantyRepository;
import group12.ecwms.moonpham.features.warranty.repository.WarrantyTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarrantyServiceImpl implements WarrantyService {

    private final DigitalWarrantyRepository digitalWarrantyRepository;
    private final WarrantyTicketRepository warrantyTicketRepository;

    @Override
    public List<DigitalWarranty> listForUser(Long userId, String searchQuery) {
        String q = searchQuery == null ? "" : searchQuery.trim();
        if (q.isEmpty()) {
            return digitalWarrantyRepository.findAllByUserIdWithProduct(userId);
        }
        return digitalWarrantyRepository.findAllByUserIdWithProductSearch(userId, q);
    }

    @Override
    public Optional<DigitalWarranty> findDetailForUser(Long userId, Long warrantyId) {
        return digitalWarrantyRepository.findByIdAndUserIdWithDetails(warrantyId, userId);
    }

    @Override
    public List<WarrantyTicket> listTickets(Long warrantyId) {
        return warrantyTicketRepository.findByDigitalWarranty_IdOrderByCreatedAtDesc(warrantyId);
    }

    @Override
    public boolean isWarrantyExpired(DigitalWarranty warranty) {
        if (warranty.getWarrantyStatus() == WarrantyStatus.expired
                || warranty.getWarrantyStatus() == WarrantyStatus.VOID) {
            return true;
        }
        LocalDate end = warranty.getWarrantyEndDate();
        return end != null && LocalDate.now().isAfter(end);
    }

    @Override
    @Transactional
    public String createSupportTicket(Long userId, Long warrantyId, String issueType, String description) {
        DigitalWarranty dw = digitalWarrantyRepository.findByIdAndUserIdWithDetails(warrantyId, userId)
                .orElse(null);
        if (dw == null) {
            return "Warranty not found.";
        }
        if (isWarrantyExpired(dw)) {
            return "This warranty has expired. You cannot submit a warranty request.";
        }
        if (issueType == null || issueType.isBlank()) {
            return "Please enter the issue type.";
        }
        String desc = description == null ? "" : description.trim();
        if (desc.length() < 10) {
            return "Please describe the problem in at least 10 characters.";
        }
        WarrantyTicket t = new WarrantyTicket();
        t.setDigitalWarranty(dw);
        t.setTicketCode("TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        t.setIssueType(issueType.trim());
        t.setDescription(desc);
        t.setTicketStatus(TicketStatus.NEW);
        warrantyTicketRepository.save(t);
        return null;
    }
}
