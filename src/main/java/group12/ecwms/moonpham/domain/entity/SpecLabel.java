package group12.ecwms.moonpham.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "spec_labels")
@Getter
@Setter
@NoArgsConstructor
public class SpecLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spec_group_id", nullable = false)
    private SpecGroup specGroup;

    @Column(nullable = false, length = 120)
    private String name;
}

