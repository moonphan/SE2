package group12.ecwms.moonpham.features.products.repository;

import group12.ecwms.moonpham.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Page<Product> findAllBySubCategory_Id(Long subCategoryId, Pageable pageable);

    List<Product> findTop5BySubCategory_IdOrderByTotalSoldDesc(Long subCategoryId);
}

