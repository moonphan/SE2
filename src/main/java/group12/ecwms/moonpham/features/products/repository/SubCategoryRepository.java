package group12.ecwms.moonpham.features.products.repository;

import group12.ecwms.moonpham.domain.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findAllByOrderByNameAsc();
}

