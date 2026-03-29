package group12.ecwms.moonpham.features.products.repository;

import group12.ecwms.moonpham.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Page<Product> findAllBySubCategory_Id(Long subCategoryId, Pageable pageable);

    List<Product> findTop5BySubCategory_IdOrderByTotalSoldDesc(Long subCategoryId);

    @Query("""
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.category
            LEFT JOIN FETCH p.subCategory
            LEFT JOIN FETCH p.servicePackages
            WHERE p.id = :id
            """)
    Optional<Product> findDetailedById(@Param("id") Long id);
}

