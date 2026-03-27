package group12.ecwms.moonpham.domain.entity;

import group12.ecwms.moonpham.domain.enums.TicketStatus;
import group12.ecwms.moonpham.domain.enums.TicketStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "warranty_tickets")
@Getter
@Setter
@NoArgsConstructor
public class WarrantyTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "digital_warranty_id", nullable = false)
    private DigitalWarranty digitalWarranty;

    @Column(name = "ticket_code", length = 50, unique = true)
    private String ticketCode;

    @Column(name = "issue_type", nullable = false, length = 100)
    private String issueType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String attachments;

    @Convert(converter = TicketStatusConverter.class)
    @Column(name = "ticket_status", nullable = false, length = 20)
    private TicketStatus ticketStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (ticketStatus == null) ticketStatus = TicketStatus.NEW;
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

