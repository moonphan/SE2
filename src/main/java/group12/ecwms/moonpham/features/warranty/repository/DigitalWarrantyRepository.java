package group12.ecwms.moonpham.features.warranty.repository;

import group12.ecwms.moonpham.domain.entity.DigitalWarranty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DigitalWarrantyRepository extends JpaRepository<DigitalWarranty, Long> {

    @Query("""
            SELECT dw FROM DigitalWarranty dw
            JOIN FETCH dw.product p
            LEFT JOIN FETCH p.category
            WHERE dw.user.id = :userId
            ORDER BY dw.createdAt DESC
            """)
    List<DigitalWarranty> findAllByUserIdWithProduct(@Param("userId") Long userId);

    @Query("""
            SELECT dw FROM DigitalWarranty dw
            JOIN FETCH dw.product p
            LEFT JOIN FETCH p.category
            WHERE dw.user.id = :userId
            AND (
                LOWER(dw.serialNumber) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            ORDER BY dw.createdAt DESC
            """)
    List<DigitalWarranty> findAllByUserIdWithProductSearch(@Param("userId") Long userId, @Param("q") String q);

    @Query("""
            SELECT dw FROM DigitalWarranty dw
            JOIN FETCH dw.product p
            LEFT JOIN FETCH p.category
            LEFT JOIN FETCH p.subCategory
            JOIN FETCH dw.orderItem oi
            JOIN FETCH oi.order o
            WHERE dw.id = :id AND dw.user.id = :userId
            """)
    Optional<DigitalWarranty> findByIdAndUserIdWithDetails(@Param("id") Long id, @Param("userId") Long userId);

    boolean existsByOrderItem_Id(Long orderItemId);
}
