package group12.ecwms.moonpham.features.products.repository;

import group12.ecwms.moonpham.domain.entity.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {

    List<ServicePackage> findByProduct_Id(Long productId);

    Optional<ServicePackage> findFirstByProduct_IdOrderByPriceAsc(Long productId);
}

