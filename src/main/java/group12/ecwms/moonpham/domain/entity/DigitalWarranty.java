package group12.ecwms.moonpham.domain.entity;

import group12.ecwms.moonpham.domain.enums.WarrantyStatus;
import group12.ecwms.moonpham.domain.enums.WarrantyStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "digital_warranties")
@Getter
@Setter
@NoArgsConstructor
public class DigitalWarranty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_item_id", nullable = false, unique = true)
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "serial_number", nullable = false, unique = true, length = 120)
    private String serialNumber;

    @Column(name = "warranty_start_date", nullable = false)
    private LocalDate warrantyStartDate;

    @Column(name = "warranty_end_date", nullable = false)
    private LocalDate warrantyEndDate;

    @Convert(converter = WarrantyStatusConverter.class)
    @Column(name = "warranty_status", nullable = false, length = 20)
    private WarrantyStatus warrantyStatus;

    @Column(name = "repair_history", columnDefinition = "TEXT")
    private String repairHistory;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (warrantyStartDate == null) warrantyStartDate = LocalDate.now();
        if (warrantyStatus == null) warrantyStatus = WarrantyStatus.active;
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

