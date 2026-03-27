package group12.ecwms.moonpham.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "spec_groups")
@Getter
@Setter
@NoArgsConstructor
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;

    @Column(nullable = false, length = 120)
    private String name;

    @OneToMany(mappedBy = "specGroup", fetch = FetchType.LAZY)
    private List<SpecLabel> specLabels;
}

